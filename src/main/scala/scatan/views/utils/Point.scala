package scatan.views.utils

import scatan.model.map.{Hexagon, StructureSpot}

/** @param value
  *   the value of the double to wrap.
  */
final case class DoubleWithPrecision(value: Double, precision: Int = 3):
  private val absDiff: Double = math.pow(10, -precision)
  private val roundConst = math.pow(10, precision)
  override def equals(x: Any): Boolean =
    x match
      case that: DoubleWithPrecision =>
        (this.value - that.value).abs < absDiff
      case _ => false

  override def hashCode: Int = (value * roundConst).round.toInt.hashCode

/** A trait to represent cartesian coordinates.
  */
trait Point:
  def x: DoubleWithPrecision
  def y: DoubleWithPrecision

object Point:

  /** Create a new Coordinates object.
    *
    * @param x
    *   the x coordinate
    * @param y
    *   the y coordinate
    * @return
    *   the new Coordinates object
    */
  def apply(x: DoubleWithPrecision, y: DoubleWithPrecision): Point = PointImpl(x, y)

  /** Extract the x and y coordinates from a Coordinates object, unwrapping them to double.
    *
    * @param coordinates
    *   the coordinates to extract from
    * @return
    *   a pair of doubles.
    */
  def unapply(point: Point): (Double, Double) =
    (point.x.value, point.y.value)

  private case class PointImpl(x: DoubleWithPrecision, y: DoubleWithPrecision) extends Point

  /** Convert a pair of doubles to a Coordinates object.
    */
  given Conversion[(Double, Double), Point] with
    def apply(pair: (Double, Double)): Point =
      Point(DoubleWithPrecision(pair._1), DoubleWithPrecision(pair._2))

  /** Extension methods to handle hexagon in cartesian plane.
    */
  extension (hex: Hexagon)
    /** Get the center, based on cantesian coordinates of the Hexagon.
      *
      * @return
      *   the center point of the hexagon
      */
    def center(using hexSize: Int): Point =
      val x = hexSize * (math.sqrt(3) * hex.col + math.sqrt(3) / 2 * hex.row)
      val y = hexSize * (3.0 / 2 * hex.row)
      (x, y)

    /** Get the vertices of the hexagon.
      *
      * @return
      *   the points of the hexagon
      */
    def vertices(using hexSize: Int): Set[Point] =
      val Point(x, y) = center
      for
        i <- (0 to 5).toSet
        angle_deg = 60 * i - 30
        angle_rad = (math.Pi / 180.0) * angle_deg
      yield (
        x + hexSize * math.cos(angle_rad),
        y + hexSize * math.sin(angle_rad)
      )

  /** Extension methods to handle spot in cartesian plane.
    */
  extension (spot: StructureSpot)
    def point(using hexSize: Int): Point =
      (spot._1.vertices & spot._2.vertices & spot._3.vertices).head
