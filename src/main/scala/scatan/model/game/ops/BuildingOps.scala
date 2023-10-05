package scatan.model.game.ops

import scatan.model.components.{AssignedBuildings, AssignmentInfo, BuildingType, Cost}
import scatan.model.game.ScatanState
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.ops.EmptySpotsOps.{emptyRoadSpot, emptyStructureSpot}
import scatan.model.game.ops.AwardOps.*
import scatan.model.map.{RoadSpot, Spot, StructureSpot}

object BuildingOps:
  extension (state: ScatanState)

    /** Verifies if a player has enough resources to pay a certain cost.
      *
      * @param player
      *   the player to verify the resources of
      * @param cost
      *   the cost to verify
      * @return
      *   true if the player has enough resources to pay the cost, false otherwise
      */
    private def verifyResourceCost(player: ScatanPlayer, cost: Cost): Boolean =
      cost.foldLeft(true)((result, resourceCost) =>
        result && state.resourceCards(player).count(_.resourceType == resourceCost._1) >= resourceCost._2
      )

    def build(position: Spot, buildingType: BuildingType, player: ScatanPlayer): Option[ScatanState] =
      if verifyResourceCost(player, buildingType.cost) then
        val remainingResourceCards = buildingType.cost.foldLeft(state.resourceCards(player))((cards, resourceCost) =>
          cards.filter(_.resourceType != resourceCost._1).drop(resourceCost._2)
        )
        val gameWithConsumedResources =
          state.copy(resourceCards = state.resourceCards.updated(player, remainingResourceCards))
        gameWithConsumedResources.assignBuilding(position, buildingType, player)
      else None

    def assignBuilding(spot: Spot, buildingType: BuildingType, player: ScatanPlayer): Option[ScatanState] =
      val buildingUpdated =
        spot match
          case s: RoadSpot if state.emptyRoadSpot.contains(s) =>
            state.assignedBuildings.updated(s, AssignmentInfo(player, buildingType))
          case s: StructureSpot if state.emptyStructureSpot.contains(s) =>
            state.assignedBuildings.updated(s, AssignmentInfo(player, buildingType))
          case _ => state.assignedBuildings

      if buildingUpdated == state.assignedBuildings then None
      else
        Some(
          state.copy(
            assignedBuildings = buildingUpdated,
            assignedAwards = state.awards
          )
        )
