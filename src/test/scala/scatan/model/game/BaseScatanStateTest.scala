package scatan.model.game

import scatan.BaseTest
import scatan.model.components.BuildingType
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.state.ScatanState
import scatan.model.game.state.ops.BuildingOps.assignBuilding
import scatan.model.game.state.ops.EmptySpotOps.{emptyRoadSpots, emptySpots}
import scatan.model.map.{RoadSpot, Spot, StructureSpot}

abstract class BaseScatanStateTest extends BaseTest:

  protected def players(n: Int): Seq[ScatanPlayer] =
    (1 to n).map(i => ScatanPlayer(s"Player $i"))

  protected def noConstrainToBuildRoad: ScatanState => ((RoadSpot, ScatanPlayer) => Boolean) = _ => (_, _) => true
  protected def noConstrainToBuildSettlment: ScatanState => ((StructureSpot, ScatanPlayer) => Boolean) = _ =>
    (_, _) => true
  protected def spotShouldBeEmptyToBuildRoad: ScatanState => ((RoadSpot, ScatanPlayer) => Boolean) = s =>
    (r, _) => s.emptyRoadSpots.contains(r)

  // Avoid heavy check on spot type
  extension (state: ScatanState)
    def assignRoadWithoutRule(spot: RoadSpot, player: ScatanPlayer): Option[ScatanState] =
      state.assignBuilding(spot, BuildingType.Road, player, roadBuildingRules = noConstrainToBuildRoad)
    def assignSettlmentWithoutRule(spot: StructureSpot, player: ScatanPlayer): Option[ScatanState] =
      state.assignBuilding(spot, BuildingType.Settlement, player, settlementBuildingRules = noConstrainToBuildSettlment)

  protected def emptySpot(state: ScatanState): Spot = state.emptySpots.head

  val threePlayers = players(3)
  val fourPlayers = players(4)
