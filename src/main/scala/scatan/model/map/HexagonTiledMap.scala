package scatan.model.map

import scatan.utils.UnorderedTriple
import scatan.utils.UnorderedPair
import scatan.model.map.HexagonInMap.*

/** An Hexagon in the space rapresented by 3 coordinates.
  */
trait Hexagon:
  def row: Int
  def col: Int
  def slice: Int

object Hexagon:
  import cats.Monoid
  given Monoid[Hexagon] with
    def empty: Hexagon = Hexagon(0, 0, 0)
    def combine(hex1: Hexagon, hex2: Hexagon): Hexagon =
      Hexagon(
        hex1.row + hex2.row,
        hex1.col + hex2.col,
        hex1.slice + hex2.slice
      )
  def apply(row: Int, col: Int, slice: Int): Hexagon = HexagonImpl(row, col, slice)
  private case class HexagonImpl(row: Int, col: Int, slice: Int) extends Hexagon

/** A Spot is unique identified by three hexagons in the map.
  *
  * @param hexagons
  */
type Spot = UnorderedTriple[Hexagon]

/** A Road connect two spots.
  */
type Road = UnorderedPair[Spot]

/** A map made of hexagonal tiles.
  */
class HexagonalTiledMap(layers: Int) extends UndirectedGraph[Spot, Road]:

  val tiles: Set[Hexagon] =
    (for
      r <- -layers to layers
      q <- -layers to layers
      s <- -layers to layers
      if r + q + s == 0
    yield Hexagon(r, q, s)).toSet

  val nodes: Set[Spot] =
    for
      hex <- tiles
      first <- hex.neighbours
      second <- hex.neighbours
      if first.isNeighbour(second)
    yield UnorderedTriple(hex, first, second)

  val spotsPerRoad = 2
  val edges: Set[Road] =
    for
      first <- nodes
      second <- nodes
      if first.toSet.intersect(second.toSet).sizeIs == spotsPerRoad
    yield UnorderedPair(first, second)

/** Extension method of Hexagons if put in an Hexagonal map.
  */
object HexagonInMap:
  private def allowedDirections: Set[Hexagon] = Set(
    Hexagon(+1, 0, -1),
    Hexagon(+1, -1, 0),
    Hexagon(0, -1, +1),
    Hexagon(-1, 0, +1),
    Hexagon(-1, +1, 0),
    Hexagon(0, +1, -1)
  )

  /** An Hexagon acquire space knowledge.
    */
  extension (hex: Hexagon)
    /** @return
      *   the set of all neighbours
      */
    def neighbours: Set[Hexagon] =
      import Hexagon.given
      import cats.syntax.semigroup.*
      allowedDirections.map(hex |+| _)

    /** Neighborhood means that two Hexagon are far away 1 in coordinate system
      * @param another
      *   Hexagon to test on
      * @return
      *   true if another is a neighbour of this hexagon
      */
    def isNeighbour(another: Hexagon): Boolean = hex.neighbours.contains(another)

    /** @return
      *   the distance from the center
      */
    def layer: Int = (math.abs(hex.row) + math.abs(hex.col) + math.abs(hex.slice)) / 2
