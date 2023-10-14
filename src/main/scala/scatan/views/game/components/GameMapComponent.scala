package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.model.map.GameMap
import scatan.model.components.{AssignmentInfo, BuildingType, Terrain}
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.state.ScatanState
import scatan.model.map.*
import scatan.views.utils.Coordinates
import scatan.views.utils.Coordinates.*
import scatan.views.utils.TypeUtils.*
import scatan.views.game.components.MapComponent.{MapElement, radius, given}

/** A component to display the game map.
  */
object GameMapComponent:

  /** An anti corruption layer that maps the model concept to the view ones.
    */
  private object ModelContextMapping:
    private var viewPlayers: Map[ScatanPlayer, String] = Map.empty
    private val buildings: Map[BuildingType, String] = Map(
      BuildingType.Settlement -> "S",
      BuildingType.City -> "C"
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

  import ModelContextMapping.*

  /** Display the game map.
    * @return
    *   the component.
    */
  def mapComponent: DisplayableSource[Element] =
    div(
      className := "game-view-game-tab",
      child <-- gameViewModel.state.map(_.state).map(gameHexagonalMap(using clickHandler)(using _))
    )

  private def gameMap(using ScatanState): GameMap = scatanState.gameMap
  private def robberPlacement(using ScatanState): Hexagon = summon[ScatanState].robberPlacement
  private def assignmentInfoOf(spot: Spot)(using ScatanState): Option[AssignmentInfo] =
    summon[ScatanState].assignedBuildings.get(spot)

  private def gameHexagonalMap: InputSourceWithState[Element] =
    given GameMap = gameMap
    MapComponent.mapContainer(
      for hex <- gameMap.tiles.toList
      yield svgHexagonWithCrossedNumber(hex),
      for road <- gameMap.edges.toList
      yield svgRoad(road),
      for spot <- gameMap.nodes.toList
      yield svgSpot(spot)
    )

  /** A svg hexagon with a number inside.
    *
    * @param hex,
    *   the hexagon
    * @return
    *   the svg hexagon.
    */
  private def svgHexagonWithCrossedNumber(hex: Hexagon): InputSourceWithState[MapElement] =
    MapComponent.svgHexagon(hex, circularNumberWithRobber(hex))

  /** A svg circular number with a robber cross.
    * @param hex
    *   the hexagon
    * @return
    *   the component
    */
  private def circularNumberWithRobber(hex: Hexagon): InputSourceWithState[MapElement] =
    MapComponent.circularNumber(
      hex,
      onClick --> (_ => clickHandler.onHexagonClick(hex)),
      if robberPlacement == hex
      then robberCross
      else ""
    )

  /** @return
    *   the Element representing the robber's cross on the game map.
    */
  private def robberCross: Element =
    svg.g(
      svg.className := "robber",
      svg.line(
        svg.x1 := s"-$radius",
        svg.y1 := s"-$radius",
        svg.x2 := s"$radius",
        svg.y2 := s"$radius"
      ),
      svg.line(
        svg.x1 := s"-$radius",
        svg.y1 := s"$radius",
        svg.x2 := s"$radius",
        svg.y2 := s"-$radius"
      )
    )

  /** Create a svg road.
    * @param road
    *   the road
    * @return
    *   the svg road
    */
  private def svgRoad(road: RoadSpot): InputSourceWithState[Element] =
    val Coordinates(x1, y1) = road._1.coordinates.get
    val Coordinates(x2, y2) = road._2.coordinates.get
    val player = assignmentInfoOf(road).map(_.viewPlayer)
    svg.g(
      svg.line(
        svg.x1 := s"$x1",
        svg.y1 := s"$y1",
        svg.x2 := s"$x2",
        svg.y2 := s"$y2",
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

  /** Create a svg spot for a structure.
    * @param structure
    *   the spot
    * @return
    *   the svg spot
    */
  private def svgSpot(structure: StructureSpot): InputSourceWithState[Element] =
    val Coordinates(x, y) = structure.coordinates.get
    val player = assignmentInfoOf(structure).map(_.viewPlayer)
    val structureType = assignmentInfoOf(structure).map(_.viewBuildingType)
    svg.g(
      svg.circle(
        svg.cx := s"$x",
        svg.cy := s"$y",
        svg.r := s"$radius",
        svg.className := s"${player.getOrElse("spot")}",
        onClick --> (_ => clickHandler.onStructureClick(structure))
      ),
      svg.text(
        svg.x := s"$x",
        svg.y := s"$y",
        svg.className := "spot-text",
        svg.fontSize := s"$radius",
        s"${structureType.getOrElse("")}"
      )
    )
