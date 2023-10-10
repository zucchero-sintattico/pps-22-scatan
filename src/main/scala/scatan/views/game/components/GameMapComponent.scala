package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.controllers.game.{GameController, PositioningHandler}
import scatan.model.components.ResourceType.*
import scatan.model.components.UnproductiveTerrain.*
import scatan.model.components.{AssignmentInfo, BuildingType, Terrain}
import scatan.model.game.ScatanState
import scatan.model.game.config.ScatanPlayer
import scatan.model.map.{Hexagon, StructureSpot, TileContent}
import scatan.model.{ApplicationState, GameMap}
import scatan.views.Coordinates
import scatan.views.Coordinates.*
import scatan.views.game.components.ContextMap.{toImgId, viewBuildingType, viewPlayer}

object ContextMap:

  private var viewPlayers: Map[ScatanPlayer, String] = Map.empty
  private val buildings: Map[BuildingType, String] = Map(
    BuildingType.Settlement -> "S",
    BuildingType.City -> "C"
  )

  val resources: Map[Terrain, String] = Map(
    Wood -> "res/img/hexagonal/wood.jpg",
    Sheep -> "res/img/hexagonal/sheep.jpg",
    Wheat -> "res/img/hexagonal/wheat.jpg",
    Rock -> "res/img/hexagonal/ore.jpg",
    Brick -> "res/img/hexagonal/clay.jpg",
    Desert -> "res/img/hexagonal/desert.jpg",
    Sea -> "res/img/hexagonal/water.jpg"
  )

  private def updateAndGetPlayer(player: ScatanPlayer): String =
    viewPlayers.get(player) match
      case Some(viewPlayer) => viewPlayer
      case None =>
        val viewPlayer = s"player-${viewPlayers.size + 1}"
        viewPlayers = viewPlayers + (player -> viewPlayer)
        viewPlayer

  extension (state: ScatanState) def gameMap: GameMap = state.gameMap
  extension (info: AssignmentInfo)
    def viewPlayer: String = updateAndGetPlayer(info.player)
    def viewBuildingType: String = buildings(info.buildingType)

  extension (terrain: Terrain) def toImgId: String = s"img-${terrain.toString.toLowerCase}"

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
      x = hexSize * math.cos(angleRad)
      y = hexSize * math.sin(angleRad)
    yield s"$x,$y").mkString(" ")
  private val layersToCanvasSize: Int => Int = x => (2 * x * hexSize) + 50

  def mapComponent(using reactiveState: Signal[ApplicationState])(using handler: PositioningHandler): Element =
    div(
      className := "game-view-game-tab",
      child <-- reactiveState
        .map(state =>
          (for
            game <- state.game
            state = game.state
          yield getHexagonalMap(state)).getOrElse(div("No game"))
        )
    )

  private def getHexagonalMap(state: ScatanState)(using handler: PositioningHandler): Element =
    val gameMap = state.gameMap
    val canvasSize = layersToCanvasSize(gameMap.totalLayers)
    svg.svg(
      svgImages,
      svg.viewBox := s"-${canvasSize} -${canvasSize} ${2 * canvasSize} ${2 * canvasSize}",
      for
        hex <- gameMap.tiles.toList
        content = gameMap.toContent(hex)
        hasRobber = state.robberPlacement == hex
      yield svgHexagonWithNumber(hex, content, hasRobber),
      for
        road <- gameMap.edges.toList
        spot1Coordinates <- road._1.coordinates
        spot2Coordinates <- road._2.coordinates
        player = state.assignedBuildings.get(road).map(_.viewPlayer)
      yield svgRoad(spot1Coordinates, spot2Coordinates, player, () => handler.onRoadSpot(road)),
      for
        spot <- gameMap.nodes.toList
        coordinates <- spot.coordinates
        assignmentInfo = state.assignedBuildings.get(spot)
        player = assignmentInfo.map(_.viewPlayer)
        buildingType = assignmentInfo.map(_.viewBuildingType)
      yield svgSpot(coordinates, player, buildingType, () => handler.onStructureSpot(spot))
    )

  /** A svg hexagon.
    *
    * @param hex,
    *   the hexagon
    * @return
    *   the svg hexagon.
    */
  private def svgHexagonWithNumber(hex: Hexagon, tileContent: TileContent, hasRobber: Boolean): Element =
    val Coordinates(x, y) = hex.center
    svg.g(
      svg.transform := s"translate($x, $y)",
      svg.polygon(
        svg.points := svgCornersPoints,
        svg.cls := "hexagon",
        svg.fill := s"url(#${tileContent.terrain.toImgId})"
      ),
      tileContent.terrain match
        case Sea => ""
        case _   => circularNumberWithRobber(tileContent.number, hasRobber)
    )

  /** A svg circular number
    * @param number,
    *   the number to display
    * @return
    */
  private def circularNumberWithRobber(number: Option[Int], hasRobber: Boolean): Element =
    svg.g(
      svg.circle(
        svg.cx := "0",
        svg.cy := "0",
        svg.r := s"$radius",
        svg.className := "hexagon-center-circle"
      ),
      svg.text(
        svg.x := "0",
        svg.y := "0",
        svg.fontSize := s"$radius",
        svg.className := "hexagon-center-number",
        number.map(_.toString).getOrElse("")
      ),
      if hasRobber then robberCross else ""
    )

  private def robberCross: Element =
    svg.g(
      svg.className := "robber",
      svg.line(
        svg.x1 := s"-${radius}",
        svg.y1 := s"-${radius}",
        svg.x2 := s"$radius",
        svg.y2 := s"$radius"
      ),
      svg.line(
        svg.x1 := s"-${radius}",
        svg.y1 := s"${radius}",
        svg.x2 := s"$radius",
        svg.y2 := s"-$radius"
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
  private def svgRoad(
      spot1: Coordinates,
      spot2: Coordinates,
      withPlayer: Option[String],
      onRoadClick: () => Unit
  ): Element =
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
            onClick --> (_ => onRoadClick())
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
  private def svgSpot(
      coordinate: Coordinates,
      withPlayer: Option[String],
      withType: Option[String],
      onSpotClick: () => Unit
  ): Element =
    val Coordinates(x, y) = coordinate
    svg.g(
      svg.circle(
        svg.cx := s"${x}",
        svg.cy := s"${y}",
        svg.r := s"$radius",
        svg.className := s"${withPlayer.getOrElse("spot")}",
        onClick --> (_ => onSpotClick())
      ),
      svg.text(
        svg.x := s"${x}",
        svg.y := s"${y}",
        svg.className := "spot-text",
        svg.fontSize := s"$radius",
        s"${withType.getOrElse("")}"
      )
    )

  private val svgImages: Element =
    svg.svg(
      svg.defs(
        for (terrain, path) <- ContextMap.resources.toList
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
