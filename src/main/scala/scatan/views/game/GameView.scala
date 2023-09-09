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

  val hexSize = 100.0
  val gameMap = GameMap(2)

  /** Convert hexagon to point, the point is the center of the hexagon
    *
    * @param hex,
    *   the hexagon
    * @return
    *   the point of the hexagon
    */
  def fromHexToPoint(hex: Hexagon): (Double, Double) =
    val x = this.hexSize * (math.sqrt(3) * hex.col + math.sqrt(3) / 2 * hex.row)
    val y = this.hexSize * (3.0 / 2 * hex.row)
    (x, y)

  /** Get the points of the hexagon, starting from the center of the hexagon, and then clockwise
    *
    * @param hex,
    *   the hexagon
    * @return
    *   the points of the hexagon
    */
  def getPointsOfHex(hex: Hexagon): Set[(DoubleWithPrecision, DoubleWithPrecision)] =
    val (x, y) = fromHexToPoint(hex)
    val points = Set(
      (DoubleWithPrecision(x), DoubleWithPrecision(y + this.hexSize)),
      (DoubleWithPrecision(x + this.hexSize * math.sqrt(3) / 2), DoubleWithPrecision(y + this.hexSize / 2)),
      (DoubleWithPrecision(x + this.hexSize * math.sqrt(3) / 2), DoubleWithPrecision(y - this.hexSize / 2)),
      (DoubleWithPrecision(x), DoubleWithPrecision(y - this.hexSize)),
      (DoubleWithPrecision(x - this.hexSize * math.sqrt(3) / 2), DoubleWithPrecision(y - this.hexSize / 2)),
      (DoubleWithPrecision(x - this.hexSize * math.sqrt(3) / 2), DoubleWithPrecision(y + this.hexSize / 2))
    )
    points

  /** Get the point of the spot, the point is the center of the spot
    * @param spot,
    *   the spot
    * @return
    *   the point of the spot
    */
  def getPointOfSpot(spot: Spot): Option[(DoubleWithPrecision, DoubleWithPrecision)] =
    (getPointsOfHex(spot._1) & getPointsOfHex(spot._2) & getPointsOfHex(spot._3)).headOption

  /** Double with precision, used to compare double with precision 0.001
    *
    * @param value,
    *   the value of the double
    */
  case class DoubleWithPrecision(value: Double):
    override def equals(x: Any): Boolean =
      x match
        case that: DoubleWithPrecision =>
          (this.value - that.value).abs < 0.001
        case _ => false

    override def hashCode: Int = (value * 1000).toInt.hashCode

  private def drawHexagon(hex: Hexagon): Element =
    val (x, y) = fromHexToPoint(hex)
    svg.g(
      svg.transform := s"translate($x, $y) rotate(30) scale(0.95)",
      svg.polygon(
        svg.points := "100,0 50,-87 -50,-87 -100,-0 -50,87 50,87",
        svg.cls := "hexagon"
      )
    )

  private def drawRoad(
      spot1: (DoubleWithPrecision, DoubleWithPrecision),
      spot2: (DoubleWithPrecision, DoubleWithPrecision)
  ): Element =
    print(spot1, spot2)
    svg.line(
      svg.x1 := s"${spot1._1.value}",
      svg.y1 := s"${spot1._2.value}",
      svg.x2 := s"${spot2._1.value}",
      svg.y2 := s"${spot2._2.value}",
      svg.className := "road",
      svg.stroke := "red",
      svg.strokeWidth := "10"
    )

  private def drawSpot(x: DoubleWithPrecision, y: DoubleWithPrecision): Element =
    svg.circle(
      svg.cx := s"${x.value}",
      svg.cy := s"${y.value}",
      svg.r := "10",
      svg.className := "spot",
      svg.fill := "white"
    )
  override def element: Element =
    div(
      display := "block",
      width := "70%",
      margin := "auto",
      svg.svg(
        svg.viewBox := "-500 -500 1000 1000",
        for hex <- gameMap.tiles.toList
        yield drawHexagon(hex),
        for
          spots <- gameMap.edges.toList
          pointsOfSpot1 <- getPointOfSpot(spots._1)
          pointsOfSpot2 <- getPointOfSpot(spots._2)
        yield drawRoad(pointsOfSpot1, pointsOfSpot2),
        for
          spot <- gameMap.nodes.toList
          (x, y) <- getPointOfSpot(spot)
        yield drawSpot(x, y)
      )
    )
