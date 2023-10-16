# Implementazione

## Generale

### Tecnologie di dettaglio utilizzate

- [scalajs-dom](https://scala-js.github.io/scala-js-dom/)
- [Laminar](https://laminar.dev)

## Manuel Andruccioli

### Creazione Hexagonal Game Map

**Obiettivo**: creare una game map di dimensioni arbitrariamente grandi, cercando una modellazione quanto più vicina al dominio. 
Essa deve permettere di rappresentare _esagoni_, _spot_ e _strade_.
Inoltre, l'implementazione deve poter permettere di accedere in modo intuitivo alle informazioni che la mappa può fornire 

#### Tiles

La creazione della game map è partita dall'individuazione dell'unità di base, l'`Hexagon`.

L'articolo [Hexagonal Grids](https://www.redblobgames.com/grids/hexagons/) è stato di grande aiuto per la comprensione del funzionamento di una griglia esagonale e per la scelta della rappresentazione interna degli esagoni, attraverso le coordinate cubiche.

Partendo da questa base teorica è stato possibile creare la mappa a tassellazioni esagonali, con la possibilità di creare mappe di dimensioni arbitrariamente grandi.

Data una `case class Hexagon(r, c, s)` ed imponendo il vincolo:

$$ r + c + s = 0 $$

è possibile possibile generare facilmente una collezione di esagoni che rappresenta le tasselle esagonali della mappa.

```scala
val tiles: Set[Hexagon] =
    (for
        r <- -layers to layers
        c <- -layers to layers
        s <- -layers to layers
        if r + c + s == 0
    yield Hexagon(r, c, s)).toSet
```

![Hexagonal Grids](../05-implementation/../img/05-implementation/manuandru/hexagon-tiles.jpg)

#### Spot

Data la collezione di tasselle esagonali, è ora possibile determinare gli _spot_ della mappa.
Essi sono i punti di intersezione tra le esagoni e sono stati ricavati con il seguente ragionamento: dato un esagono qualsiasi \\( H \\), si prendono 2 esagoni \\( F, S \\), adiacenti ad \\( H \\), che, a loro volta, sono adiacenti tra loro.

Ne deriva il seguente codice:

```scala
val nodes: Set[StructureSpot] =
    for
      hex <- tiles
      first <- hex.neighbours
      second <- hex.neighbours
      if first.isNeighbour(second)
    yield UnorderedTriple(hex, first, second)
```

#### Road

Data la collezione di _spot_, è ora possibile determinare le _road_ della mappa.
Esse sono gli archi di collegamento tra 2 _spot_ e sono ricavati con il seguente ragionamento: dati 2 _spot_ qualsiasi \\( S_1, S_2 \\), essi formano un arco se e solo se \\( S_1 \\) e \\( S_2 \\) hanno 2 esagoni in comune.

Ne deriva il seguente codice:

```scala
val edges: Set[RoadSpot] =
    for
        first <- nodes
        second <- nodes
        if (first.toSet & second.toSet).sizeIs == 2
    yield UnorderedPair(first, second)
```
![Spot e Road](../05-implementation/../img/05-implementation/manuandru/spot-road.jpg)

### Hexagon e Monoidi

A fronte di una lettura di [Scala with Cats](https://underscore.io/books/scala-with-cats/), ho voluto sperimentare.

È stato riconosciuto come un `Hexagon` può essere un **monoide** (e garantisce proprietà utili durante la scrittura di codice), in quanto è possibile soddisfare gli assiomi di:

- **associatività**: \\( \forall \\; a,b,c \in S \Rightarrow (a \cdot b) \cdot c = a \cdot (b \cdot c) \\)
- **identità**: \\( \exists \\; \epsilon \in S : \forall a \in S \Rightarrow (\epsilon \cdot a) = a  \wedge (a \cdot \epsilon) = a \\)

Ne deriva il seguente codice, utilizzando la libreria [Cats](https://typelevel.org/cats/):

```scala
import cats.Monoid
  given Monoid[Hexagon] with
    def empty: Hexagon = Hexagon(0, 0, 0)
    def combine(hex1: Hexagon, hex2: Hexagon): Hexagon =
      Hexagon(
        hex1.row + hex2.row,
        hex1.col + hex2.col,
        hex1.slice + hex2.slice
      )

// An example of usage
def allowedDirections: Set[Hexagon]
// ...
extension (hex: Hexagon)
    def neighbours: Set[Hexagon] =
      import Hexagon.given
      import cats.syntax.semigroup.*
      allowedDirections.map(hex |+| _)
```

### GameMap in view

La visualizzazione della _GameMap_ è stata una parte delicata e complessa, in quanto è stato necessario trovare un modo per rappresentare graficamente, in modo consono, la gli esagoni, spot e road precedentemente introdotti, ma anche per gestire l'interazione dell'utente con gli elementi.

La parte di accesso al model è immediata, data la rappresentazione di esso.

```scala
MapComponent.mapContainer(
    for hex <- gameMap.tiles.toList
    yield svgHexagonWithCrossedNumber(hex),
    for road <- gameMap.edges.toList
    yield svgRoad(road),
    for spot <- gameMap.nodes.toList
    yield svgSpot(spot)
)
```

La parte più complicata è stata la rappresentazione grafica degli elementi, dovendo trovare un _mapping_ su un piano bidimensionale, basato su coordinate cartesiane (dato dalla tecnologia utilizzata, `SVG`).

Innanzitutto è stato introdotto un concetto di `Point`, un punto nel piano cartesiano, che ha permesso di rendere agevole l'utilizzo del sistema di coordinate da dover adottare.
Inoltre, è stato necessario aggiungere un wrapper di `Double` che aggiunge il concetto di precisione, in modo da consentire operazioni sul tipo, senza incorrere in errori di arrotondamento.

Infine, sono stati realizzati metodi di utilità per trattare una coppia di Double come un punto nel piano cartesiano, ma anche per scomporre un punto.

Da ciò derivata la seguente implementazione:

```scala
trait Point:
    def x: DoubleWithPrecision
    def y: DoubleWithPrecision

object Point:
    def unapply(point: Point): (Double, Double) = (point.x.value, point.y.value)

given Conversion[(Double, Double), Point] with
    def apply(pair: (Double, Double)): Point =
        Point(DoubleWithPrecision(pair._1), DoubleWithPrecision(pair._2))
```
#### Nesting di componenti

La costruzione della mappa è stata realizzata attraverso il nesting di componenti, ognuno con diverse proprietà.
In linea di massima, si è sempre cercato di separare la logica di creazione del componente, compreso di ciò che necessità per la resa grafica, dallo stile di visualizzione.

Per far fronte a ciò, ogni componente è corredato dello stretto necessario, con aggiunta di una classe che permette di personalizzare la resa grafica, attraverso i fogli di stile.

> Nota: non tutte le proprietà di svg sono modificabili via css, perciò sono state inserire direttamente nel componente.

Di seguito un esempio:

```scala
...
svg.text(
    svg.x := s"$x",
    svg.y := s"$y",
    svg.className := "spot-text",
    svg.fontSize := s"$radius",
    s"${structureType.getOrElse("")}"
)
...
```

```css
.spot-text {
    fill: black;
    text-anchor: middle;
    dominant-baseline: central;
    font-family: sans-serif;
    font-weight: bold;
}
```

### View TypeUtils

### Test

## Alessandro Mazzoli

## Luigi Borriello

## Pair programming
