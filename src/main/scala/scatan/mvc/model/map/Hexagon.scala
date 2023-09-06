package scatan.mvc.model.map

trait Hexagon:
  def row: Int
  def col: Int
  def slice: Int
  def neighbours: Set[Hexagon]
  def isNeighbour(another: Hexagon): Boolean
  def layer: Int

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

  private val allowedDirections: Set[Hexagon] = Set(
    Hexagon(+1, 0, -1),
    Hexagon(+1, -1, 0),
    Hexagon(0, -1, +1),
    Hexagon(-1, 0, +1),
    Hexagon(-1, +1, 0),
    Hexagon(0, +1, -1)
  )

  private case class HexagonImpl(row: Int, col: Int, slice: Int) extends Hexagon:
    def neighbours: Set[Hexagon] =
      import cats.syntax.semigroup.*
      allowedDirections.map((this: Hexagon) |+| _)
    def isNeighbour(another: Hexagon): Boolean = neighbours.contains(another)
    def layer: Int = (math.abs(row) + math.abs(col) + math.abs(slice)) / 2
