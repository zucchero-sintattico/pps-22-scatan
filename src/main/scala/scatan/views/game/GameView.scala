package scatan.views.game

import scatan.controllers.game.GameController

import scatan.Pages
import com.raquo.laminar.api.L.*
import scatan.mvc.lib.{ScalaJSView, View}
import scatan.model.Spot
import scatan.model.map.Hexagon
import scatan.model.GameMap

trait GameView extends View

class ScalaJsGameView(requirements: View.Requirements[GameController], container: String)
    extends GameView
    with View.Dependencies(requirements)
    with ScalaJSView(container):
  val numberOfUsers: Int = 3

  val gameMap = GameMap(2)

  def fromHexToPoint(hex: Hexagon): (Double, Double) =
    val size = 100.0
    val x = size * (math.sqrt(3) * hex.col + math.sqrt(3) / 2 * hex.row)
    val y = size * (3.0 / 2 * hex.row)
    (x, y)

  def getPointsOfHex(hex: Hexagon): Set[(DoubleWithPrecision, DoubleWithPrecision)] =
    val size = 100.0
    val x = size * (math.sqrt(3) * hex.col + math.sqrt(3) / 2 * hex.row)
    val y = size * (3.0 / 2 * hex.row)
    val points = Set(
      (DoubleWithPrecision(x), DoubleWithPrecision(y + size)),
      (DoubleWithPrecision(x + size * math.sqrt(3) / 2), DoubleWithPrecision(y + size / 2)),
      (DoubleWithPrecision(x + size * math.sqrt(3) / 2), DoubleWithPrecision(y - size / 2)),
      (DoubleWithPrecision(x), DoubleWithPrecision(y - size)),
      (DoubleWithPrecision(x - size * math.sqrt(3) / 2), DoubleWithPrecision(y - size / 2)),
      (DoubleWithPrecision(x - size * math.sqrt(3) / 2), DoubleWithPrecision(y + size / 2))
    )
    points

  def getPointOfSpot(spot: Spot): Option[(DoubleWithPrecision, DoubleWithPrecision)] =
    (getPointsOfHex(spot._1) & getPointsOfHex(spot._2) & getPointsOfHex(spot._3)).headOption

  case class DoubleWithPrecision(value: Double):
    override def equals(x: Any): Boolean =
      x match
        case that: DoubleWithPrecision =>
          (this.value - that.value).abs < 0.001
        case _ => false

    override def hashCode: Int = (value * 1000).toInt.hashCode

  override def element: Element =
    div(
      display := "block",
      width := "70%",
      margin := "auto",
      svg.svg(
        svg.viewBox := "-500 -500 1000 1000",
        for hex <- gameMap.tiles.toList
        yield
          val (x, y) = fromHexToPoint(hex)
          svg.g(
            svg.transform := s"translate($x, $y) rotate(30) scale(0.95)",
            svg.polygon(
              svg.points := "100,0 50,-87 -50,-87 -100,-0 -50,87 50,87",
              svg.cls := "hexagon"
            ),
            onClick --> (_ => println(hex))
          )
        ,
        for
          spots <- gameMap.edges.toList
          (x1, y1) <- getPointOfSpot(spots._1)
          (x2, y2) <- getPointOfSpot(spots._2)
        yield svg.g(
          svg.line(
            svg.x1 := s"${x1.value}",
            svg.y1 := s"${y1.value}",
            svg.x2 := s"${x2.value}",
            svg.y2 := s"${y2.value}",
            svg.className := "road",
            onClick --> (_ => println(spots))
          ),
          svg.circle(
            svg.cx := s"${x1.value + (x2.value - x1.value) / 2}",
            svg.cy := s"${y1.value + (y2.value - y1.value) / 2}",
            svg.r := "20",
            svg.className := "road",
            onClick --> (_ => println(spots))
          )
        ),
        for
          spot <- gameMap.nodes.toList
          (x, y) <- getPointOfSpot(spot)
        yield svg.circle(
          svg.cx := s"${x.value}",
          svg.cy := s"${y.value}",
          svg.r := "10",
          svg.className := "spot",
          onClick --> (_ => println(spot))
        )
      )
    )
