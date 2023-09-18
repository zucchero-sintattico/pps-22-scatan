package scatan.views.game.components

import scatan.model.map.Hexagon
import scatan.model.GameMap
import com.raquo.laminar.api.L.*
import scatan.views.Coordinates.*
import scatan.views.Coordinates
import scatan.model.map.Terrain.*

/** A component to display the game map.
  */
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

  def getMapComponent(gameMap: GameMap)(using hexSize: Int): Element =
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
