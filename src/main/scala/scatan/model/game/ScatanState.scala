package scatan.model.game

import cats.instances.long
import scatan.lib.game.Player
import scatan.model.components.*
import scatan.model.game.ScatanState
import scatan.model.GameMap
import scatan.model.map.Spot
import scatan.model.components.AssignedBuildingsAdapter.asPlayerMap
import scatan.model.map.Hexagon
import scatan.model.map.TileContent
import scatan.model.map.StructureSpot
import scatan.model.components.AssignedBuildingsAdapter.getStructureSpots
import scatan.model.map.HexagonInMap.layer
import scatan.model.map.RoadSpot
import scatan.model.game.state.BasicScatanState
import scatan.model.game.state.ScoreKnowledge
import scatan.model.game.state.EmptySpotsManagement
import scatan.model.game.state.AwardKnowledge

object ScatanState:
  def apply(players: Seq[Player]): ScatanState =
    require(players.sizeIs >= 3 && players.sizeIs <= 4, "The number of players must be between 3 and 4")
    ScatanState(
      players,
      GameMap(),
      Map.empty,
      Hexagon(0, 0, 0),
      ResourceCard.empty(players),
      DevelopmentCardsOfPlayers.empty(players),
      Award.empty()
    )

  def ended(_players: Seq[Player]) =
    ScatanState(
      _players,
      GameMap(),
      Map.empty,
      Hexagon(0, 0, 0),
      ResourceCard.empty(_players),
      DevelopmentCardsOfPlayers.empty(_players),
      Award.empty()
    )
final case class ScatanState(
    players: Seq[Player],
    gameMap: GameMap,
    assignedBuildings: AssignedBuildings,
    robberPlacement: Hexagon,
    resourceCards: ResourceCards,
    developmentCards: DevelopmentCards,
    assignedAwards: Awards = Award.empty()
) extends BasicScatanState[ScatanState]
    with ScoreKnowledge[ScatanState]
    with AwardKnowledge[ScatanState]:

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
    this.copy(
      resourceCards = resourceCards.updated(player, resourceCards(player) :+ resourceCard)
    )

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
    this.copy(
      developmentCards = developmentCards.updated(player, developmentCards(player) :+ developmentCard),
      assignedAwards = awards
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
    val remainingCards = developmentCards(player).filter(_.developmentType == developmentCard.developmentType).drop(1)
    this.copy(developmentCards = developmentCards.updated(player, remainingCards), assignedAwards = awards)

  /** Assigns resources to players based on the tile content of the hexagons where their buildings are located.
    * @param hexagonsWithTileContent
    *   a map of hexagons with their corresponding tile content
    * @return
    *   a new ScatanState with updated resource cards for each player
    */
  def assignResourceFromHexagons(hexagonsWithTileContent: Map[Hexagon, TileContent]) =
    val buildingsInAssignedSpots =
      assignedBuildings.filter((s, _) =>
        s match
          case structure: StructureSpot => structure.toSet.intersect(hexagonsWithTileContent.keys.toSet).nonEmpty
          case _: RoadSpot              => false
      )
    val resourceCardsUpdated =
      buildingsInAssignedSpots.foldLeft(resourceCards)((resourceOfPlayer, buildingInAssignedSpot) =>
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
    this.copy(resourceCards = resourceCardsUpdated)

  /** Assigns resources from the hexagons that have the given number and are not occupied by the robber.
    * @param number
    *   the number of the hexagons to filter by
    * @return
    *   a new ScatanState with the assigned resources
    */
  def assignResourcesFromNumber(number: Int): ScatanState =
    val hexagonsFilteredByNumber = gameMap.toContent
      .filter(
        (
            hexagon,
            tileContent
        ) => tileContent.number.isDefined && tileContent.number.get == number && hexagon != robberPlacement
      )

    assignResourceFromHexagons(hexagonsFilteredByNumber)

  /** Returns a new ScatanState with the robber moved to the specified hexagon.
    *
    * @param hexagon
    *   the hexagon to move the robber to
    * @return
    *   a new ScatanState with the robber moved to the specified hexagon
    */
  def moveRobber(hexagon: Hexagon): ScatanState = this.copy(robberPlacement = hexagon)
