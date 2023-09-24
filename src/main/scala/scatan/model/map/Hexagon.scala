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
