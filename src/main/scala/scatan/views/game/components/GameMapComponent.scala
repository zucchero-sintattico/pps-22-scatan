package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.controllers.game.GameController
import scatan.model.components.ResourceType.*
import scatan.model.components.UnproductiveTerrain.*
import scatan.model.components.{AssignedBuildings, AssignmentInfo, BuildingType, Terrain}
import scatan.model.game.ScatanState
import scatan.model.game.config.ScatanPlayer
import scatan.model.map.*
import scatan.model.{ApplicationState, GameMap}
import scatan.views.game.components.ContextMap.{toImgId, viewBuildingType, viewPlayer}
import scatan.views.utils.Coordinates
import scatan.views.utils.Coordinates.*
import scatan.views.utils.TypeUtils.*

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

  /** Display the game map.
    * @return
    *   the component.
    */
  def mapComponent: DisplayableSource[Element] =
    div(
      className := "game-view-game-tab",
      child <-- gameViewModel.state.map(_.state).map(state => {
        getHexagonalMap(using clickHandler)(using state)
      })
    )

  private def gameMap(using ScatanState): GameMap = scatanState.gameMap
  private def contentOf(hex: Hexagon)(using ScatanState): TileContent = scatanState.gameMap.toContent(hex)
  private def robberPlacement(using ScatanState): Hexagon = summon[ScatanState].robberPlacement
  private def assignmentInfoOf(spot: Spot)(using ScatanState): Option[AssignmentInfo] =
    summon[ScatanState].assignedBuildings.get(spot)

  private def getHexagonalMap: InputSourceWithState[Element] =
    val canvasSize = layersToCanvasSize(gameMap.totalLayers)
    svg.svg(
      svgImages,
      svg.viewBox := s"-${canvasSize} -${canvasSize} ${2 * canvasSize} ${2 * canvasSize}",
      for hex <- gameMap.tiles.toList
      yield svgHexagonWithNumber(hex),
      for road <- gameMap.edges.toList
      yield svgRoad(road),
      for spot <- gameMap.nodes.toList
      yield svgSpot(spot)
    )

  /** A svg hexagon.
    *
    * @param hex,
    *   the hexagon
    * @return
    *   the svg hexagon.
    */
  private def svgHexagonWithNumber(hex: Hexagon): InputSourceWithState[Element] =
    val Coordinates(x, y) = hex.center
    svg.g(
      svg.transform := s"translate($x, $y)",
      svg.polygon(
        svg.points := svgCornersPoints,
        svg.cls := "hexagon",
        svg.fill := s"url(#${contentOf(hex).terrain.toImgId})"
      ),
      contentOf(hex).terrain match
        case Sea => ""
        case _   => circularNumberWithRobber(hex)
    )

  /** A svg circular number
    * @param number,
    *   the number to display
    * @return
    */
  private def circularNumberWithRobber(hex: Hexagon): InputSourceWithState[Element] =
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
        contentOf(hex).number.map(_.toString).getOrElse("")
      ),
      onClick --> (_ => clickHandler.onHexagonClick(hex)),
      if robberPlacement == hex
      then robberCross
      else ""
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
  private def svgRoad(road: RoadSpot): InputSourceWithState[Element] =
    val Coordinates(x1, y1) = road._1.coordinates.get
    val Coordinates(x2, y2) = road._2.coordinates.get
    val player = assignmentInfoOf(road).map(_.viewPlayer)
    svg.g(
      svg.line(
        svg.x1 := s"${x1}",
        svg.y1 := s"${y1}",
        svg.x2 := s"${x2}",
        svg.y2 := s"${y2}",
        svg.className := s"road ${player.getOrElse("")}"
      ),
      player match
        case Some(_) => ""
        case _ =>
          svg.circle(
            svg.cx := s"${x1 + (x2 - x1) / 2}",
            svg.cy := s"${y1 + (y2 - y1) / 2}",
            svg.className := "road-center",
            svg.r := s"$radius",
            onClick --> (_ => clickHandler.onRoadClick(road))
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
  private def svgSpot(structure: StructureSpot): InputSourceWithState[Element] =
    val Coordinates(x, y) = structure.coordinates.get
    val player = assignmentInfoOf(structure).map(_.viewPlayer)
    val structureType = assignmentInfoOf(structure).map(_.viewBuildingType)
    svg.g(
      svg.circle(
        svg.cx := s"${x}",
        svg.cy := s"${y}",
        svg.r := s"$radius",
        svg.className := s"${player.getOrElse("spot")}",
        onClick --> (_ => clickHandler.onStructureClick(structure))
      ),
      svg.text(
        svg.x := s"${x}",
        svg.y := s"${y}",
        svg.className := "spot-text",
        svg.fontSize := s"$radius",
        s"${structureType.getOrElse("")}"
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
