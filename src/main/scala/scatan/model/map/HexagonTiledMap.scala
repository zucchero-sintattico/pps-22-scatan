package scatan.model.map

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

/** A map made of hexagonal tiles.
  */
trait HexTiledMap:
  /** @return
    *   the tiles of the map
    */
  def tiles: Set[Hexagon]

object HexTiledMap:
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
