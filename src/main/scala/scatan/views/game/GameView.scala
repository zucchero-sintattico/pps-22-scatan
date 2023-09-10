package scatan.views.game

import scatan.controllers.game.GameController

import scatan.Pages
import com.raquo.laminar.api.L.*
import scatan.mvc.lib.{ScalaJSView, View}
import scatan.model.Spot
import scatan.model.map.Hexagon
import scatan.model.GameMap
import scatan.views.game.Coordinates.center
import scatan.views.game.Coordinates.coordinates
import scatan.model.map.Terrain.*
import com.raquo.airstream.core.Signal

trait GameView extends View

/** @param value
  */
final case class DoubleWithPrecision(value: Double):
  override def equals(x: Any): Boolean =
    x match
      case that: DoubleWithPrecision =>
        (this.value - that.value).abs < 0.001
      case _ => false

  override def hashCode: Int = (value * 1000).round.toInt.hashCode

/** A trait to represent cartesian coordinates.
  */
trait Coordinates:
  def x: DoubleWithPrecision
  def y: DoubleWithPrecision

object Coordinates:

  /** Create a new Coordinates object.
    *
    * @param x
    *   the x coordinate
    * @param y
    *   the y coordinate
    * @return
    *   the new Coordinates object
    */
  def apply(x: DoubleWithPrecision, y: DoubleWithPrecision): Coordinates = CoordinatesImpl(x, y)

  /** Extract the x and y coordinates from a Coordinates object, unwrapping them to double.
    *
    * @param coordinates
    *   the coordinates to extract from
    * @return
    *   a pair of doubles.
    */
  def unapply(coordinates: Coordinates): (Double, Double) =
    (coordinates.x.value, coordinates.y.value)

  private case class CoordinatesImpl(x: DoubleWithPrecision, y: DoubleWithPrecision) extends Coordinates

  /** Convert a pair of doubles to a Coordinates object.
    */
  given Conversion[(Double, Double), Coordinates] with
    def apply(pair: (Double, Double)): Coordinates =
      Coordinates(DoubleWithPrecision(pair._1), DoubleWithPrecision(pair._2))

  /** Extension methods to handle hexagon in cartesian plane.
    */
  extension (hex: Hexagon)
    /** Get the center, based on cantesian coordinates of the Hexagon.
      *
      * @return
      *   the center point of the hexagon
      */
    def center(using hexSize: Int): Coordinates =
      val x = hexSize * (math.sqrt(3) * hex.col + math.sqrt(3) / 2 * hex.row)
      val y = hexSize * (3.0 / 2 * hex.row)
      (x, y)

    /** Get the vertices of the hexagon.
      *
      * @return
      *   the points of the hexagon
      */
    def vertices(using hexSize: Int): Set[Coordinates] =
      val Coordinates(x, y) = center
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
  extension (spot: Spot)
    def coordinates(using hexSize: Int): Option[Coordinates] =
      (spot._1.vertices & spot._2.vertices & spot._3.vertices).headOption

class ScalaJsGameView(requirements: View.Requirements[GameController], container: String)
    extends GameView
    with View.Dependencies(requirements)
    with ScalaJSView(container):

  given hexSize: Int = 100
  val gameMap = GameMap(2)

  /** Generate the hexagon graphic
    *
    * @param hex,
    *   the hexagon
    * @return
    *   the hexagon graphic
    */
  private def generateHexagon(hex: Hexagon): Element =
    val Coordinates(x, y) = hex.center
    svg.g(
      svg.transform := s"translate($x, $y)",
      svg.polygon(
        svg.points := "0,100 87,50 87,-50 0,-100 -87,-50 -87,50", // TODO: refactor?
        svg.cls := "hexagon",
        svg.fill := s"url(#img-${resources.head._1.toString.toLowerCase})" // TODO: add map from model terrain
      )
    )

  /** Generate the road graphic
    * @param spot1,
    *   the first spot
    * @param spot2,
    *   the second spot
    * @return
    *   the road graphic
    */
  private def generateRoad(spot1: Coordinates, spot2: Coordinates): Element =
    val Coordinates(x1, y1) = spot1
    val Coordinates(x2, y2) = spot2
    svg.g(
      svg.line(
        svg.x1 := s"${x1}",
        svg.y1 := s"${y1}",
        svg.x2 := s"${x2}",
        svg.y2 := s"${y2}",
        svg.className := "road"
      ),
      svg.circle(
        svg.cx := s"${x1 + (x2 - x1) / 2}",
        svg.cy := s"${y1 + (y2 - y1) / 2}",
        svg.className := "road-center",
        svg.r := "25",
        onClick --> (_ => println((spot1, spot2)))
      )
    )

  /** Generate the spot graphic
    * @param x,
    *   the x coordinate of the spot
    * @param y,
    *   the y coordinate of the spot
    * @return
    *   the spot graphic
    */
  private def generateSpot(coordinate: Coordinates): Element =
    val Coordinates(x, y) = coordinate
    svg.circle(
      svg.cx := s"${x}",
      svg.cy := s"${y}",
      svg.r := "25",
      svg.className := "spot",
      onClick --> (_ => println((x, y)))
    )

  val resources = Map(
    WOOD -> "res/img/hexagonal/wood.jpg",
    SHEEP -> "res/img/hexagonal/sheep.jpg",
    GRAIN -> "res/img/hexagonal/wheat.jpg",
    ROCK -> "res/img/hexagonal/ore.jpg",
    BRICK -> "res/img/hexagonal/clay.jpg",
    DESERT -> "res/img/hexagonal/desert.jpg"
  )

  private val svgImages: Element =
    svg.svg(
      svg.defs(
        for (terrain, path) <- resources.toList
        yield svg.pattern(
          svg.idAttr := s"img-${terrain.toString.toLowerCase}",
          svg.width := "100%",
          svg.height := "100%",
          svg.patternContentUnits := "objectBoundingBox",
          svg.image(
            svg.href := path,
            svg.width := "1",
            svg.height := "1",
            svg.preserveAspectRatio := "none"
          )
        )
      )
    )

  override def element: Element =
    div(
      display := "block",
      width := "70%",
      margin := "auto",
      svg.svg(
        svgImages,
        svg.viewBox := "-500 -500 1000 1000",
        for hex <- gameMap.tiles.toList
        yield generateHexagon(hex),
        for
          spots <- gameMap.edges.toList
          pointsOfSpot1 <- spots._1.coordinates
          pointsOfSpot2 <- spots._2.coordinates
        yield generateRoad(pointsOfSpot1, pointsOfSpot2),
        for
          spot <- gameMap.nodes.toList
          coordinates <- spot.coordinates
        yield generateSpot(coordinates)
      )
    )
