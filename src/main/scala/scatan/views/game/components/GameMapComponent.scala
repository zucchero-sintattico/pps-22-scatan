package scatan.views.game.components

import scatan.model.map.Hexagon
import scatan.model.GameMap
import com.raquo.laminar.api.L.*
import scatan.views.Coordinates.*
import scatan.views.Coordinates

object GameMapComponent:
  /** Generate the hexagon graphic
    *
    * @param hex,
    *   the hexagon
    * @return
    *   the hexagon graphic
    */
  private def generateHexagon(hex: Hexagon)(using hexSize: Int): Element =
    val Coordinates(x, y) = hex.center
    svg.g(
      svg.transform := s"translate($x, $y) rotate(30) scale(0.95)",
      svg.polygon(
        svg.points := "100,0 50,-87 -50,-87 -100,-0 -50,87 50,87",
        svg.cls := "hexagon"
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
        svg.className := "road",
        svg.stroke := "red",
        svg.strokeWidth := "10"
      ),
      svg.circle(
        svg.cx := s"${x1 + (x2 - x1) / 2}",
        svg.cy := s"${y1 + (y2 - y1) / 2}",
        svg.r := "15",
        svg.className := "road",
        svg.fill := "yellow",
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
      svg.r := "10",
      svg.className := "spot",
      svg.fill := "white",
      onClick --> (_ => println((x, y)))
    )

  def getMapComponent(gameMap: GameMap)(using hexSize: Int): Element =
    svg.svg(
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
