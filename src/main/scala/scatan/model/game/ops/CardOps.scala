package scatan.model.game.ops

import scatan.model.components.{BuildingType, DevelopmentCard, ResourceCard, ResourceType}
import scatan.model.game.ScatanState
import scatan.model.game.config.ScatanPlayer
import scatan.model.map.{Hexagon, RoadSpot, StructureSpot, TileContent}
import scatan.model.game.ops.AwardOps.*

object CardOps:

  extension (state: ScatanState)

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

    /** Assigns resources to players based on the tile content of the hexagons where their buildings are located.
      * @param hexagonsWithTileContent
      *   a map of hexagons with their corresponding tile content
      * @return
      *   Some(ScatanState) if the resources were assigned, None otherwise
      */
    def assignResourceFromHexagons(hexagonsWithTileContent: Map[Hexagon, TileContent]): Option[ScatanState] =
      val buildingsInHexagonsSpots =
        state.assignedBuildings.filter((s, _) =>
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
      assignResourceFromHexagons(hexagonsFilteredByNumber)

    /** Returns a new ScatanState with the given development card assigned to the given player. The development card is
      * added to the player's list of development cards. The assigned awards are updated.
      *
      * @param player
      *   The player to assign the development card to.
      * @param developmentCard
      *   The development card to assign to the player.
      * @return
      *   Some(ScatanState) if the player added the development card, None otherwise
      */
    def assignDevelopmentCard(player: ScatanPlayer, developmentCard: DevelopmentCard): Option[ScatanState] =
      Some(
        state.copy(
          developmentCards = state.developmentCards.updated(player, state.developmentCards(player) :+ developmentCard),
          assignedAwards = state.awards
        )
      )

    /** Consumes a development card for a given player and returns a new ScatanState with the updated development cards
      * and assigned awards.
      * @param player
      *   the player who is consuming the development card
      * @param developmentCard
      *   the development card to be consumed
      * @return
      *   Some(ScatanState) if the player has the development card, None otherwise
      */
    def consumeDevelopmentCard(player: ScatanPlayer, developmentCard: DevelopmentCard): Option[ScatanState] =
      if !state.developmentCards(player).contains(developmentCard) then None
      else
        val remainingCards =
          state.developmentCards(player).filter(_.developmentType == developmentCard.developmentType).drop(1)
        Some(
          state.copy(
            developmentCards = state.developmentCards.updated(player, remainingCards),
            assignedAwards = state.awards
          )
        )
