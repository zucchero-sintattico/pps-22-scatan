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

    /** Builds a building of a certain type on a certain spot for a certain player. If the player has not enough
      * resources to pay the cost of the building, the building is not built. If the player has enough resources to pay
      * the cost of the building, the building is built and the resources are consumed.
      *
      * @param position
      * @param buildingType
      * @param player
      * @return
      *   Some(ScatanState) if the building is built, None otherwise
      */
    def build(position: Spot, buildingType: BuildingType, player: ScatanPlayer): Option[ScatanState] =
      if verifyResourceCost(player, buildingType.cost) then
        val remainingResourceCards = buildingType.cost.foldLeft(state.resourceCards(player))((cards, resourceCost) =>
          cards.filter(_.resourceType != resourceCost._1).drop(resourceCost._2)
        )
        val gameWithBuildingAssigned = assignBuilding(position, buildingType, player)
        gameWithBuildingAssigned match
          case Some(game) =>
            Some(
              game.copy(
                resourceCards = game.resourceCards.updated(player, remainingResourceCards),
                assignedAwards = game.awards
              )
            )
          case None => None
      else None

    /** Assigns a building of a certain type on a certain spot for a certain player. If the spot is not empty, the
      * building is not assigned. If the spot is empty, the building is assigned. If the building is a city, the spot
      * must contain a settlement of the same player.
      *
      * @param spot
      * @param buildingType
      * @param player
      * @return
      *   Some(ScatanState) if the building is assigned, None otherwise
      */
    def assignBuilding(spot: Spot, buildingType: BuildingType, player: ScatanPlayer): Option[ScatanState] =
      buildingType match
        case BuildingType.City =>
          state.assignedBuildings(spot) match
            case AssignmentInfo(`player`, BuildingType.Settlement) =>
              Some(
                state.copy(
                  assignedBuildings = state.assignedBuildings.updated(spot, AssignmentInfo(player, buildingType)),
                  assignedAwards = state.awards
                )
              )
            case _ => None
        case BuildingType.Settlement =>
          if state.emptyStructureSpot.contains(spot) then
            Some(
              state.copy(
                assignedBuildings = state.assignedBuildings.updated(spot, AssignmentInfo(player, buildingType)),
                assignedAwards = state.awards
              )
            )
          else None
        case BuildingType.Road =>
          if state.emptyRoadSpot.contains(spot) then
            Some(
              state.copy(
                assignedBuildings = state.assignedBuildings.updated(spot, AssignmentInfo(player, buildingType)),
                assignedAwards = state.awards
              )
            )
          else None
