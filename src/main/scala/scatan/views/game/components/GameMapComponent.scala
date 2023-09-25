package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.model.GameMap
import scatan.model.map.Hexagon
import scatan.model.map.TileContent
import scatan.model.components.ResourceType.*
import scatan.model.components.UnproductiveTerrain.*
import scatan.model.components.Terrain
import scatan.views.Coordinates
import scatan.views.Coordinates.*
import scatan.views.game.components.GameMapComponent.BuildingType.*

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

  private val players: Map[Int, String] = Map(
    0 -> "player1",
    1 -> "player2",
    2 -> "player3",
    3 -> "player4"
  )

  enum BuildingType:
    case Settlement, City
  private val buildings: Map[BuildingType, String] = Map(
    Settlement -> "S",
    City -> "C"
  )

  def getMapComponent(gameMap: GameMap): Element =
    val canvasSize = layersToCanvasSize(gameMap.totalLayers)
    svg.svg(
      svgImages,
      svg.viewBox := s"-${canvasSize} -${canvasSize} ${2 * canvasSize} ${2 * canvasSize}",
      for
        hex <- gameMap.tiles.toList
        content = gameMap.toContent(hex)
      yield svgHexagonWithNumber(hex, content),
      for
        spots <- gameMap.edges.toList
        spot1Coordinates <- spots._1.coordinates
        spot2Coordinates <- spots._2.coordinates
        player = players.get((math.random() * 10).toInt) // TODO: correct player selection
      yield svgRoad(spot1Coordinates, spot2Coordinates, player),
      for
        spot <- gameMap.nodes.toList
        coordinates <- spot.coordinates
        player = players.get((math.random() * 10).toInt) // TODO: correct player selection
        building = buildings.get(
          buildings.keySet.zipWithIndex
            .find(_._2 == (math.random() * 2).toInt)
            .getOrElse((Settlement, 0))
            ._1
        ) // TODO: correct building selection
      yield svgSpot(
        coordinates,
        player,
        if player.isDefined then building else None
      ) // TODO: correct building selection
    )

  /** A svg hexagon.
    *
    * @param hex,
    *   the hexagon
    * @return
    *   the svg hexagon.
    */
  private def svgHexagonWithNumber(hex: Hexagon, tileContent: TileContent): Element =
    val Coordinates(x, y) = hex.center
    svg.g(
      svg.transform := s"translate($x, $y)",
      svg.polygon(
        svg.points := svgCornersPoints,
        svg.cls := "hexagon",
        svg.fill := s"url(#${tileContent.terrain.toImgId})"
      ),
      tileContent.number match
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
  private def svgRoad(spot1: Coordinates, spot2: Coordinates, withPlayer: Option[String]): Element =
    val Coordinates(x1, y1) = spot1
    val Coordinates(x2, y2) = spot2
    svg.g(
      svg.line(
        svg.x1 := s"${x1}",
        svg.y1 := s"${y1}",
        svg.x2 := s"${x2}",
        svg.y2 := s"${y2}",
        svg.className := s"road ${withPlayer.getOrElse("")}"
      ),
      withPlayer match
        case Some(_) => ""
        case _ =>
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
  private def svgSpot(coordinate: Coordinates, withPlayer: Option[String], withType: Option[String]): Element =
    val Coordinates(x, y) = coordinate
    svg.g(
      svg.circle(
        svg.cx := s"${x}",
        svg.cy := s"${y}",
        svg.r := s"$radius",
        svg.className := s"${withPlayer.getOrElse("spot")}",
        onClick --> (_ => println((x, y)))
      ),
      svg.text(
        svg.x := s"${x}",
        svg.y := s"${y}",
        svg.textAnchor := "middle",
        svg.dominantBaseline := "central",
        svg.fontFamily := "sans-serif",
        svg.fontSize := s"$radius",
        svg.fontWeight := "bold",
        svg.fill := "black",
        s"${withType.getOrElse("")}"
      )
    )

  private val resources: Map[Terrain, String] = Map(
    Wood -> "res/img/hexagonal/wood.jpg",
    Sheep -> "res/img/hexagonal/sheep.jpg",
    Wheat -> "res/img/hexagonal/wheat.jpg",
    Rock -> "res/img/hexagonal/ore.jpg",
    Brick -> "res/img/hexagonal/clay.jpg",
    Desert -> "res/img/hexagonal/desert.jpg",
    Sea -> "res/img/hexagonal/water.jpg"
  )

  extension (terrain: Terrain) def toImgId: String = s"img-${terrain.toString.toLowerCase}"

  private val svgImages: Element =
    svg.svg(
      svg.defs(
        for (terrain, path) <- resources.toList
        yield svg.pattern(
          svg.idAttr := terrain.toImgId,
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
