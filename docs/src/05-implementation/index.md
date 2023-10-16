# Implementazione

## Generale

### Tecnologie di dettaglio utilizzate

- [scalajs-dom](https://scala-js.github.io/scala-js-dom/)
- [Laminar](https://laminar.dev)

## Manuel Andruccioli

Il codice prodotto durante lo svolgimento del progetto riguarda prevalentemente le seguente parti:

- Gestione della parte statica della mappa di gioco
  - Esagoni, Spot e Road
  - Mappa come un grafo
  - Contenuto degli esagoni
  - Creazione di essa
- Regole sul posizionamento delle strutture
- Gestione visualizzazione della mappa
- Preview della mappa con possibilità di scelta
- Gestione della CI per building, testing e deployment

Di seguito saranno descritte con maggior dettaglio le parti più salienti.

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

Ne deriva il seguente codice, utilizzando la libreria [`Cats`](https://typelevel.org/cats/):

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

La creazione della view, che necessità di un nesting dei componenti, risulta parecchio prolissa e ripetitiva nel passaggio dei parametri.

Per ovviare a questo problema, ho scelto di nascondere il passaggio dei parametri ridondanti, attraverso l'utilizzo di [`Context Function`](https://docs.scala-lang.org/scala3/reference/contextual/context-functions.html) e la dichiarazione di opportuni tipi, a seconda dell'elemento di contesto che si vuole catturare.

Inoltre, sono stati definiti anche tipi dati da composizioni degli stessi, in modo da catturare più elementi di contesto contemporaneamente.

Alcuni di essi sono ripoortati di seguito:

```scala
  type Displayable[T] = ScatanViewModel ?=> T
  type InputSource[T] = GameViewClickHandler ?=> T
  type DisplayableSource[T] = Displayable[InputSource[T]]
  type InputSourceWithState[T] = InputSource[GameStateKnowledge[T]]
```

La possibilità di catturare il contesto di cui ha bisogno una gerarchia nestata di chiamate a funzioni, in un tipo implicito, ha aperto la possibilità alla realizzazione di semplificazioni.

Di seguito è riportato un frammento esemplificativo:

```scala
def method1: InputSourceWithState[E] =
    // not using ScatanState
    method2

def method2: InputSourceWithState[E] =
    doSomethingWithState(summon[ScatanState])
```

Al contrario, la versione senza Context Function sarebbe stata:

```scala
def method1(state: ScatanState): E =
    // not using ScatanState
    method2(state)

def method2(state: ScatanState): E =
    doSomethingWithState(state)
```

L'esempio è tratto da una semplificazione di `scatan.views.game.components.GameMapComponent#svgHexagonWithCrossedNumber`.

### Test

Il testing, in alcune sue parti, risultava complicato da esprimere perché, ciò che si voleva porre sotto osservazione, erano delle proprietà.

> Esempio: verifica degli assiomi di un _Monoide_

Per ovviare a questo problema ho deciso di esplorare la libreria [`ScalaTest + ScalaCheck`](https://www.scalatest.org/plus/scalacheck), che permette un'integrazione tra i due framework.
Così facendo è stato possibile definire dei test **property-based**.

Di seguito un esempio di test:

```scala
class HexagonTest extends BaseTest with ScalaCheckPropertyChecks:
    "An Hexagon" should "respect the identity law in order to be a Monoid" in {
    forAll { (r: Int, c: Int, s: Int) =>
      val hexagon = Hexagon(r, c, s)
      hexagon |+| Monoid[Hexagon].empty shouldBe hexagon
      Monoid[Hexagon].empty |+| hexagon shouldBe hexagon
    }
  }
```

Qualora un test fallisce, è possibile ottenere i valori che hanno causato il fallimento e, grazie alla pseudo-randomicità, è possibile riprodurre il test.

## Alessandro Mazzoli

## Luigi Borriello

Per quanto riguarda il mio contributo al progetto, mi sono occupato principalmente delle seguenti parti:

- Creazione e modellazione dei singoli componenti relativi allo stato della partita, e delle loro corrispondenti operazioni nonchè:
  - Gestione delle carte risorse
  - Gestione delle carte sviluppo
  - Gestione delle costruzioni
  - Gestione dei punteggi
  - Gestione dei certificati
  - Gestione degli scambi intra-giocatore
  - Gestione degli scambi con la banca

- Modellazione dello stato della partita
- Realizzazione grafica degli scambi


Di seguito saranno descritte con maggior dettaglio le parti più salienti.

### Creazione e modellazione dei singoli componenti della partita

Come prima cosa, ho individuato quelle che sarebbero state le componenti principali necessari a modellare dello stato della partita, individuando come entità principali i **buildings**, le **resource cards**, le **development cards**, i **trades**, gli **awards** e gli **scores**.
Una volta individuati, ho subito organizzato le eventuali strutture dati necessarie a modellarli, cercando di mantenere una certa coerenza tra di esse, e soprattutto con il dominio del gioco.

Dopo di che, per facilitare la lettura e sviluppo del codice stesso, ho optato per definire per ognuno dei componenti, dei __type alias__, corrispondenti a codeste strutture dati, in modo da poterle utilizzare in modo più semplice e diretto.

Di seguito, sono riportati due esempi di definizione di  __type alias__:
- `ResourceCards`:
```scala
/** Type of possible resources.
  */
enum ResourceType:
  case Wood
  case Brick
  case Sheep
  case Wheat
  case Rock

/** A resource card.
  */
final case class ResourceCard(resourceType: ResourceType)

/** The resource cards hold by the players.
  */
type ResourceCards = Map[ScatanPlayer, Seq[ResourceCard]]
```

- `Awards`:
```scala
/** Type of possible awards.
  */
enum AwardType:
  case LongestRoad
  case LargestArmy

/** An award
  */
final case class Award(awardType: AwardType)

/** The assigned awards to the current holder player and the number of points.
  */
type Awards = Map[Award, Option[(ScatanPlayer, Int)]]
```

### Modellazione dello stato della partita

In concomitanza alla realizzazione di questi componenti, ho iniziato a modellare anche quella che sarebbe stata l'entità principale dello stato della partita, scegliendo di utilizzare una **case class** chiamata `ScatanState`, contenente solo le informazioni necessarie per poter catturare i vari snapshot dello stato della partita durante il suo svolgimento.

```scala
final case class ScatanState(
    players: Seq[ScatanPlayer],
    gameMap: GameMap,
    assignedBuildings: AssignedBuildings,
    assignedAwards: Awards,
    resourceCards: ResourceCards,
    developmentCards: DevelopmentCards,
    developmentCardsDeck: DevelopmentCardsDeck,
    robberPlacement: Hexagon
)
```

### Realizzazione delle ScatanState Ops

Dopo aver individuato quelle che sarebbero state le principali operazioni da poter effettuare sullo stato della partita, ho deciso di raggrupparle e dividerle in più moduli, ognuno relativo ad una specifica sotto-parte del dominio. Riuscendo così a rendere le varie funzionalità indipendenti (o semi-indipendenti) tra loro.

Per fare ciò, ho realizzato all'interno del package `scatan.model.game.state.ops` una serie di **object** ognuno dei quali contiene una serie di **extension methods** per la case class `ScatanState`, in modo da poterla arricchire di funzionalità.

Inoltre, per favorire un approccio più funzionale, ho deciso di realizzare tutti questi metodi senza side-effect, facendo in modo che ogni volta che verrà effettuata una modifica allo stato della partita, verrà ritornato un `Option[ScatanState]` contenente il nuovo stato della partita, o `None` altrimenti, permettendo agli strati superiori catturare eventuali errori e gestirli di conseguenza.

Di seguito, viene riportato un esempio di definizione dell' **object** contenente le **extension methods** per la gestione delle **resource cards**:

```scala
/** Operations on [[ScatanState]] related to resource cards.
  */
object ResourceCardOps:

  extension (state: ScatanState)
    /** Assigns a resource card to a player.
      */
    def assignResourceCard(player: ScatanPlayer, resourceCard: ResourceCard): Option[ScatanState] =
      Some(
        state.copy(
          resourceCards = state.resourceCards.updated(player, state.resourceCards(player) :+ resourceCard)
        )
      )

    /** Removes a resource card from a player.
      */
    def removeResourceCard(player: ScatanPlayer, resourceCard: ResourceCard): Option[ScatanState] =
      if !state.resourceCards(player).contains(resourceCard) then None
      else
        val remainingCardsOfSameType =
          state.resourceCards(player).filter(_.resourceType == resourceCard.resourceType).drop(1)
        val remainingCardsOfDifferentType =
          state.resourceCards(player).filter(_.resourceType != resourceCard.resourceType)
        Some(
          state.copy(
            resourceCards =
              state.resourceCards.updated(player, remainingCardsOfDifferentType ++ remainingCardsOfSameType)
          )
        )
```


### Realizzazione degli scambi
FoldLeft????

### Calcolo degli Scores






## Pair programming
