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

  private given hexSize: Int = 100
  private val radius = hexSize / 4
  private val svgCornersPoints: String =
    (for
      i <- 0 to 5
      angleDeg = 60 * i + 30
      angleRad = Math.PI / 180 * angleDeg
      x = hexSize * Math.cos(angleRad)
      y = hexSize * Math.sin(angleRad)
    yield s"$x,$y").mkString(" ")
  private val layersToCanvasSize: Int => Int = x => (2 * x * hexSize) + 50

  def getMapComponent(gameMap: GameMap): Element =
    val canvasSize = layersToCanvasSize(gameMap.layers)
    svg.svg(
      svgImages,
      svg.viewBox := s"-${canvasSize} -${canvasSize} ${2 * canvasSize} ${2 * canvasSize}",
      for hex <- gameMap.tiles.toList
      yield svgHexagonWithNumber(hex, Some(0)),
      for
        spots <- gameMap.edges.toList
        spot1Coordinates <- spots._1.coordinates
        spot2Coordinates <- spots._2.coordinates
      yield svgRoad(spot1Coordinates, spot2Coordinates),
      for
        spot <- gameMap.nodes.toList
        coordinates <- spot.coordinates
      yield svgSpot(coordinates)
    )

  /** A svg hexagon.
    *
    * @param hex,
    *   the hexagon
    * @return
    *   the svg hexagon.
    */
  private def svgHexagonWithNumber(hex: Hexagon, number: Option[Int]): Element =
    val Coordinates(x, y) = hex.center
    svg.g(
      svg.transform := s"translate($x, $y)",
      svg.polygon(
        svg.points := svgCornersPoints,
        svg.cls := "hexagon",
        svg.fill := s"url(#img-${resources.head._1.toString.toLowerCase})" // TODO: add map from model terrain
      ),
      number match
        case Some(n) => circularNumber(n)
        case _       => ""
    )

  /** A svg circular number
    * @param number,
    *   the number to display
    * @return
    */
  private def circularNumber(number: Int): Element =
    svg.g(
      svg.circle(
        svg.cx := "0",
        svg.cy := "0",
        svg.r := s"$radius",
        svg.className := "hexagon-number",
        svg.fill := "white"
      ),
      svg.text(
        svg.x := "0",
        svg.y := "0",
        svg.textAnchor := "middle",
        svg.dominantBaseline := "central",
        svg.fontFamily := "sans-serif",
        svg.fontSize := s"$radius",
        svg.fontWeight := "bold",
        svg.fill := "black",
        s"$number"
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
  private def svgRoad(spot1: Coordinates, spot2: Coordinates): Element =
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
        svg.r := s"$radius",
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
  private def svgSpot(coordinate: Coordinates): Element =
    val Coordinates(x, y) = coordinate
    svg.circle(
      svg.cx := s"${x}",
      svg.cy := s"${y}",
      svg.r := s"$radius",
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
