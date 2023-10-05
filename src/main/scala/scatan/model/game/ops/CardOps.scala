package scatan.model.game.ops

import scatan.model.game.ScatanState
import scatan.lib.game.Player
import scatan.model.components.ResourceCard
import scatan.model.components.DevelopmentCard
import scatan.model.map.{Hexagon, TileContent, StructureSpot, RoadSpot}
import scatan.model.components.ResourceType
import scatan.model.components.BuildingType

object CardOps:

  extension (state: ScatanState)
    /** Assigns a resource card to a player and returns a new ScatanState with the updated resourceCards map.
      *
      * @param player
      *   the player to assign the resource card to
      * @param resourceCard
      *   the resource card to assign to the player
      * @return
      *   a new ScatanState with the updated resourceCards map
      */
    def assignResourceCard(player: Player, resourceCard: ResourceCard): ScatanState =
      state.copy(
        resourceCards = state.resourceCards.updated(player, state.resourceCards(player) :+ resourceCard)
      )

    /** Assigns resources to players based on the tile content of the hexagons where their buildings are located.
      * @param hexagonsWithTileContent
      *   a map of hexagons with their corresponding tile content
      * @return
      *   a new ScatanState with updated resource cards for each player
      */
    def assignResourceFromHexagons(hexagonsWithTileContent: Map[Hexagon, TileContent]) =
      val buildingsInAssignedSpots =
        state.assignedBuildings.filter((s, _) =>
          s match
            case structure: StructureSpot => structure.toSet.intersect(hexagonsWithTileContent.keys.toSet).nonEmpty
            case _: RoadSpot              => false
        )
      val resourceCardsUpdated =
        buildingsInAssignedSpots.foldLeft(state.resourceCards)((resourceOfPlayer, buildingInAssignedSpot) =>
          buildingInAssignedSpot._1 match
            case structure: StructureSpot =>
              val resourceToAdd = structure.toSet.toList.collect(hexagonsWithTileContent)
              var resourceCardsOfPlayerUpdated = resourceOfPlayer
              resourceToAdd.foreach { r =>
                r match
                  case TileContent(terrain: ResourceType, _) =>
                    buildingInAssignedSpot._2.buildingType match
                      case BuildingType.Settlement =>
                        resourceCardsOfPlayerUpdated = resourceCardsOfPlayerUpdated.updated(
                          buildingInAssignedSpot._2.player,
                          resourceCardsOfPlayerUpdated(buildingInAssignedSpot._2.player) :+ ResourceCard(terrain)
                        )
                      case BuildingType.City =>
                        resourceCardsOfPlayerUpdated = resourceCardsOfPlayerUpdated.updated(
                          buildingInAssignedSpot._2.player,
                          resourceCardsOfPlayerUpdated(buildingInAssignedSpot._2.player) :+ ResourceCard(
                            terrain
                          ) :+ ResourceCard(
                            terrain
                          )
                        )
                      case BuildingType.Road =>
                  case _ =>
              }
              resourceCardsOfPlayerUpdated
            case _ => resourceOfPlayer
        )
      state.copy(resourceCards = resourceCardsUpdated)

    /** Assigns resources from the hexagons that have the given number and are not occupied by the robber.
      * @param number
      *   the number of the hexagons to filter by
      * @return
      *   a new ScatanState with the assigned resources
      */
    def assignResourcesFromNumber(number: Int): ScatanState =
      val hexagonsFilteredByNumber = state.gameMap.toContent
        .filter(
          (
              hexagon,
              tileContent
          ) => tileContent.number.isDefined && tileContent.number.get == number && hexagon != state.robberPlacement
        )

      assignResourceFromHexagons(hexagonsFilteredByNumber)

    /** Returns a new ScatanState with the given development card assigned to the given player. The development card is
      * added to the player's list of development cards. The assigned awards remain the same.
      *
      * @param player
      *   The player to assign the development card to.
      * @param developmentCard
      *   The development card to assign to the player.
      * @return
      *   A new ScatanState with the development card assigned to the player.
      */
    def assignDevelopmentCard(player: Player, developmentCard: DevelopmentCard): ScatanState =
      state.copy(
        developmentCards = state.developmentCards.updated(player, state.developmentCards(player) :+ developmentCard),
        assignedAwards = state.awards
      )

    /** Consumes a development card for a given player and returns a new ScatanState with the updated development cards
      * and assigned awards.
      * @param player
      *   the player who is consuming the development card
      * @param developmentCard
      *   the development card to be consumed
      * @return
      *   a new ScatanState with the updated development cards and assigned awards
      */
    def consumeDevelopmentCard(player: Player, developmentCard: DevelopmentCard): ScatanState =
      val remainingCards =
        state.developmentCards(player).filter(_.developmentType == developmentCard.developmentType).drop(1)
      state.copy(
        developmentCards = state.developmentCards.updated(player, remainingCards),
        assignedAwards = state.awards
      )
