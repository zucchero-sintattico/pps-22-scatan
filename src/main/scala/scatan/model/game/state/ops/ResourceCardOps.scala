package scatan.model.game.state.ops

import scatan.model.components.*
import scatan.model.components.BuildingType.Road
import scatan.model.components.DevelopmentType.Knight
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.state.ScatanState
import scatan.model.game.state.ops.AwardsOps.*
import scatan.model.game.state.ops.BuildingOps.build
import scatan.model.game.state.ops.RobberOps.moveRobber
import scatan.model.map.{Hexagon, RoadSpot, StructureSpot, TileContent}

import scala.util.Random

object ResourceCardOps:

  extension (state: ScatanState)
    /** Stole a card from a victim.
      * @param currentPlayer
      *   the player who stole the card
      * @param victim
      *   the player who lost the card
      * @return
      *   Some(ScatanState) if the card was stolen, None otherwise
      */
    def stoleResourceCard(currentPlayer: ScatanPlayer, victim: ScatanPlayer): Option[ScatanState] =
      val victimResourceCards = state.resourceCards(victim)
      if victimResourceCards.sizeIs == 0 then None
      else
        val randomCardIndex = Random.nextInt(victimResourceCards.size)
        val stolenCard = victimResourceCards(randomCardIndex)
        val updatedVictimResourceCards = victimResourceCards.zipWithIndex.filterNot(_._2 == randomCardIndex).map(_._1)
        val updatedCurrentPlayerResourceCards = state.resourceCards(currentPlayer) :+ stolenCard
        Some(
          state.copy(
            resourceCards = state.resourceCards
              .updated(victim, updatedVictimResourceCards)
              .updated(
                currentPlayer,
                updatedCurrentPlayerResourceCards
              )
          )
        )

    /** Assigns a resource card to a player.
      * @param player
      *   the player to assign the resource card to
      * @param resourceCard
      *   the resource card to assign
      * @return
      *   Some(ScatanState) if the resource card was assigned, None otherwise
      */
    def assignResourceCard(player: ScatanPlayer, resourceCard: ResourceCard): Option[ScatanState] =
      Some(
        state.copy(
          resourceCards = state.resourceCards.updated(player, state.resourceCards(player) :+ resourceCard)
        )
      )

    /** Removes a resource card from a player.
      *
      * @param player
      *   the player to remove the resource card from
      * @param resourceCard
      *   the resource card to remove
      * @return
      *   Some(ScatanState) if the resource card was removed, None otherwise
      */
    def removeResourceCard(player: ScatanPlayer, resourceCard: ResourceCard): Option[ScatanState] =
      if !state.resourceCards(player).contains(resourceCard) then None
      else
        val remainingCardsOfSameType =
          state.resourceCards(player).filter(_.resourceType == resourceCard.resourceType).drop(1)
        val remainingCardsOfDifferentType =
          state.resourceCards(player).filter(_.resourceType != resourceCard.resourceType)
        Some(
          state.copy(
            resourceCards =
              state.resourceCards.updated(player, remainingCardsOfDifferentType ++ remainingCardsOfSameType)
          )
        )

    /** Assigns resources to players based on the tile content of the hexagons where their buildings are located.
      * @param hexagonsWithTileContent
      *   a map of hexagons with their corresponding tile content
      * @return
      *   Some(ScatanState) if the resources were assigned, None otherwise
      */
    private def assignResourceFromHexagonsAndBuildings(
        hexagonsWithTileContent: Map[Hexagon, TileContent] = state.gameMap.toContent,
        buildings: AssignedBuildings = state.assignedBuildings
    ): Option[ScatanState] =
      val buildingsInHexagonsSpots =
        buildings.filter((s, _) =>
          s match
            case structure: StructureSpot => structure.toSet.intersect(hexagonsWithTileContent.keys.toSet).nonEmpty
            case _: RoadSpot              => false
        )
      val resourceCardsUpdated =
        buildingsInHexagonsSpots.foldLeft(state.resourceCards)((resourceOfPlayer, buildingInAssignedSpot) =>
          buildingInAssignedSpot._1 match
            case structure: StructureSpot =>
              val resourceToAdd = structure.toSet.collect(hexagonsWithTileContent)
              resourceToAdd.foldLeft(resourceOfPlayer)((resourceOfPlayer, resource) =>
                resource match
                  case TileContent(terrain: ResourceType, _) =>
                    buildingInAssignedSpot._2.buildingType match
                      case BuildingType.Settlement =>
                        resourceOfPlayer.updated(
                          buildingInAssignedSpot._2.player,
                          resourceOfPlayer(buildingInAssignedSpot._2.player) :+ ResourceCard(terrain)
                        )
                      case BuildingType.City =>
                        resourceOfPlayer.updated(
                          buildingInAssignedSpot._2.player,
                          resourceOfPlayer(buildingInAssignedSpot._2.player) :+ ResourceCard(
                            terrain
                          ) :+ ResourceCard(
                            terrain
                          )
                        )
                      case BuildingType.Road => resourceOfPlayer
                  case _ => resourceOfPlayer
              )
            case _ => resourceOfPlayer
        )
      Some(state.copy(resourceCards = resourceCardsUpdated))

    /** Assigns resources from the hexagons that have the given number and are not occupied by the robber.
      * @param number
      *   the number of the hexagons to filter by
      * @return
      *   Some(ScatanState) if the resources were assigned, None otherwise
      */
    def assignResourcesFromNumber(number: Int): Option[ScatanState] =
      val hexagonsFilteredByNumber = state.gameMap.toContent
        .filter(
          (
              hexagon,
              tileContent
          ) => tileContent.number.fold(false)(_ == number) && hexagon != state.robberPlacement
        )
      assignResourceFromHexagonsAndBuildings(hexagonsWithTileContent = hexagonsFilteredByNumber)

    /** Assigns resources to players based on the tile content of the hexagons where their buildings are located. This
      * method is used only for the initial placement of the buildings.
      *
      * @return
      *   Some(ScatanState) if the resources were assigned, None otherwise
      */
    def assignResourcesAfterInitialPlacement: Option[ScatanState] =
      // take only the last Structure Building for each player
      val structureBuildingCount =
        state.assignedBuildings.count((_, building) => building.buildingType == BuildingType.Settlement)
      val buildings = state.assignedBuildings
        .filter((_, building) => building.buildingType == BuildingType.Settlement)
        .takeRight(structureBuildingCount / 2)
      assignResourceFromHexagonsAndBuildings(buildings = buildings)
