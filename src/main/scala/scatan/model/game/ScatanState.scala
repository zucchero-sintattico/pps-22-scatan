package scatan.model.game

import cats.instances.long
import scatan.lib.game.Player
import scatan.model.components.*
import scatan.model.game.{ScatanState, ScatanStateImpl}
import scatan.model.GameMap
import scatan.model.map.Spot
import scatan.model.components.AssignedBuildingsAdapter.asPlayerMap
import scatan.model.map.Hexagon
import scatan.model.map.TileContent
import scatan.model.map.StructureSpot
import scatan.model.components.AssignedBuildingsAdapter.getStructureSpots

trait ScatanState:
  def players: Seq[Player]
  def emptySpots: Seq[Spot]
  def assignedBuildings: AssignedBuildings
  def robberPlacement: Hexagon
  def moveRobber(hexagon: Hexagon): ScatanState
  def developmentCards: DevelopmentCards
  def resourceCards: ResourceCards
  def awards: Awards
  def gameMap: GameMap
  def scores: Scores
  def build(position: Spot, buildingType: BuildingType, player: Player): ScatanState
  def assignResourcesFromNumber(diceRoll: Int): ScatanState
  def assignBuilding(position: Spot, buildingType: BuildingType, player: Player): ScatanState
  def assignResourceCard(player: Player, resourceCard: ResourceCard): ScatanState
  def assignDevelopmentCard(player: Player, developmentCard: DevelopmentCard): ScatanState
  def consumeDevelopmentCard(player: Player, developmentCard: DevelopmentCard): ScatanState
  def isOver: Boolean = scores.exists(_._2 >= 10)
  def winner: Option[Player] = if isOver then Some(scores.maxBy(_._2)._1) else None

object ScatanState:
  def apply(players: Seq[Player]): ScatanState =
    require(players.sizeIs >= 3 && players.sizeIs <= 4, "The number of players must be between 3 and 4")
    ScatanStateImpl(
      players,
      GameMap(),
      Map.empty,
      Hexagon(0, 0, 0),
      ResourceCard.empty(players),
      DevelopmentCardsOfPlayers.empty(players),
      Award.empty()
    )

  def apply(
      players: Seq[Player],
      gameMap: GameMap,
      assignedBuildings: AssignedBuildings,
      robberPlacement: Hexagon,
      resourceCards: ResourceCards,
      developmentCardsOfPlayers: DevelopmentCards
  ): ScatanState =
    ScatanStateImpl(players, gameMap, assignedBuildings, robberPlacement, resourceCards, developmentCardsOfPlayers)

  def ended(_players: Seq[Player]) =
    new ScatanState:
      val state = ScatanState.apply(_players)
      export state.*
      override def isOver: Boolean = true

private final case class ScatanStateImpl(
    players: Seq[Player],
    gameMap: GameMap,
    assignedBuildings: AssignedBuildings,
    robberPlacement: Hexagon,
    resourceCards: ResourceCards,
    developmentCards: DevelopmentCards,
    assignedAwards: Awards = Award.empty()
) extends ScatanState:

  def emptySpots: Seq[Spot] =
    Seq(gameMap.nodes, gameMap.edges).flatten
      .filter(!assignedBuildings.isDefinedAt(_))

  /** Returns a map of the current awards in the game, including the longest road and largest army. The longest road is
    * awarded to the player with the longest continuous road of at least 5 segments. The largest army is awarded to the
    * player with the most knight development cards played.
    * @return
    *   a map of the current awards in the game
    */
  def awards: Awards =
    val precedentLongestRoad = assignedAwards(Award(AwardType.LongestRoad))
    val longestRoad =
      assignedBuildings.asPlayerMap.foldLeft(precedentLongestRoad.getOrElse((Player(""), 0)))(
        (playerWithLongestRoad, buildingsOfPlayer) =>
          val roads = buildingsOfPlayer._2.filter(_ == BuildingType.Road)
          if roads.sizeIs > playerWithLongestRoad._2 then (buildingsOfPlayer._1, roads.size)
          else playerWithLongestRoad
      )
    val precedentLargestArmy = assignedAwards(Award(AwardType.LargestArmy))
    val largestArmy =
      developmentCards.foldLeft(precedentLargestArmy.getOrElse(Player(""), 0))((playerWithLargestArmy, cardsOfPlayer) =>
        val knights = cardsOfPlayer._2.filter(_.developmentType == DevelopmentType.Knight)
        if knights.sizeIs > playerWithLargestArmy._2 then (cardsOfPlayer._1, knights.size)
        else playerWithLargestArmy
      )
    Map(
      Award(AwardType.LongestRoad) -> (if longestRoad._2 >= 5 then Some((longestRoad._1, longestRoad._2))
                                       else precedentLongestRoad),
      Award(AwardType.LargestArmy) -> (if largestArmy._2 >= 3 then Some((largestArmy._1, largestArmy._2))
                                       else precedentLargestArmy)
    )

  /** Verifies if a player has enough resources to pay a certain cost.
    *
    * @param player
    *   the player to verify the resources of
    * @param cost
    *   the cost to verify
    * @return
    *   true if the player has enough resources to pay the cost, false otherwise
    */
  private def verifyResourceCost(player: Player, cost: Cost): Boolean =
    cost.foldLeft(true)((result, resourceCost) =>
      result && resourceCards(player).count(_.resourceType == resourceCost._1) >= resourceCost._2
    )

  /** Returns a new ScatanState with the specified building assigned to the specified player at the specified spot.
    *
    * @param spot
    *   The spot where the building will be assigned.
    * @param buildingType
    *   The type of building to be assigned.
    * @param player
    *   The player to whom the building will be assigned.
    * @return
    *   A new ScatanState with the specified building assigned to the specified player at the specified spot.
    */
  override def assignBuilding(spot: Spot, buildingType: BuildingType, player: Player): ScatanState =
    this.copy(
      assignedBuildings = assignedBuildings + AssignmentFactory(spot, player, buildingType),
      assignedAwards = awards
    )

  /** Builds a new ScatanState with the specified building at the specified position for the specified player. If the
    * player has enough resources to build the specified building, the resources are consumed and the building is
    * assigned to the player. Otherwise, the current state is returned.
    *
    * @param position
    *   The position where the building will be built.
    * @param buildingType
    *   The type of building to be built.
    * @param player
    *   The player who will build the building.
    * @return
    *   A new ScatanState with the specified building at the specified position for the specified player, or the current
    *   state if the player does not have enough resources.
    */
  override def build(position: Spot, buildingType: BuildingType, player: Player): ScatanState =
    if verifyResourceCost(player, buildingType.cost) then
      val remainingResourceCards = buildingType.cost.foldLeft(resourceCards(player))((cards, resourceCost) =>
        cards.filter(_.resourceType != resourceCost._1).drop(resourceCost._2)
      )
      val gameWithConsumedResources = this.copy(resourceCards = resourceCards.updated(player, remainingResourceCards))
      gameWithConsumedResources.assignBuilding(position, buildingType, player)
    else this

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

  /** Calculates the partial scores of the players with the awards they have received.
    * @return
    *   a `Scores` object representing the partial scores of the players with the awards they have received.
    */
  private def partialScoresWithAwards: Scores =
    val playersWithAwards = awards.filter(_._2.isDefined).map(_._2.get)
    playersWithAwards.foldLeft(Score.empty(players))((scores, playerWithCount) =>
      scores.updated(playerWithCount._1, scores(playerWithCount._1) + 1)
    )

  /** Calculates the partial scores of each player, taking into account the buildings they have assigned. The score for
    * each building type is as follows:
    *   - Settlement: 1 point
    *   - City: 2 points
    *   - Road: 0 points
    * @return
    *   a Scores object containing the partial scores of each player
    */
  private def partialScoresWithBuildings: Scores =
    def buildingScore(buildingType: BuildingType): Int = buildingType match
      case BuildingType.Settlement => 1
      case BuildingType.City       => 2
      case BuildingType.Road       => 0
    assignedBuildings.asPlayerMap.foldLeft(Score.empty(players))((scores, buildingsOfPlayer) =>
      scores.updated(
        buildingsOfPlayer._1,
        buildingsOfPlayer._2.foldLeft(0)((score, buildingType) => score + buildingScore(buildingType))
      )
    )

  /** Returns the total scores of the players in the game. The scores are calculated by combining the partial scores
    * with awards and buildings.
    * @return
    *   the total scores of the players
    */
  def scores: Scores =
    import cats.syntax.semigroup.*
    import scatan.model.components.Score.given
    val partialScores = Seq(partialScoresWithAwards, partialScoresWithBuildings)
    partialScores.foldLeft(Score.empty(players))(_ |+| _)

  /** Returns a map of structure spots with their corresponding tile content from a map of hexagons with their tile
    * content.
    *
    * @param hexagons
    *   a map of hexagons with their corresponding tile content
    * @return
    *   a map of structure spots with their corresponding tile content
    */
  private def getSpotsWithTileContentFromHexagons(
      hexagons: Map[Hexagon, TileContent]
  ): Map[StructureSpot, TileContent] =
    hexagons.foldLeft(Map.empty[StructureSpot, TileContent])((spotsWithTileContent, hexagonWithTile) =>
      val spotWithTile = (gameMap.nodes.filter(_.contains(hexagonWithTile._1)).head, hexagonWithTile._2)
      spotsWithTileContent.updated(spotWithTile._1, spotWithTile._2)
    )

  /** Assigns resources to players based on the tile content of the hexagons where their buildings are located.
    * @param hexagonsWithTileContent
    *   a map of hexagons with their corresponding tile content
    * @return
    *   a new ScatanState with updated resource cards for each player
    */
  def assignResourceFromHexagons(hexagonsWithTileContent: Map[Hexagon, TileContent]) =
    val assignedSpotsWithTileContent =
      getSpotsWithTileContentFromHexagons(hexagonsWithTileContent)
        .filter((spot, content) => assignedBuildings.contains(spot))
    val buildingsInAssignedSpots =
      assignedBuildings
        .getStructureSpots()
        .filter((structureSpot, assignmentInfo) => assignedSpotsWithTileContent.contains(structureSpot))
    val updatedResourceCards = buildingsInAssignedSpots.foldLeft(resourceCards)((resourceOfPlayer, buildingInSpot) =>
      val tileContent = assignedSpotsWithTileContent(buildingInSpot._1)
      val player = buildingInSpot._2.player
      val buildingType = buildingInSpot._2.buildingType
      if tileContent.terrain.isInstanceOf[ResourceType] then
        buildingType match
          case BuildingType.Settlement =>
            resourceOfPlayer
              .updated(
                player,
                resourceOfPlayer(player) :+ ResourceCard(tileContent.terrain.asInstanceOf[ResourceType])
              )
          case BuildingType.City =>
            resourceOfPlayer.updated(
              player,
              resourceOfPlayer(player) :+ ResourceCard(tileContent.terrain.asInstanceOf[ResourceType]) :+ ResourceCard(
                tileContent.terrain.asInstanceOf[ResourceType]
              )
            )
          case _ => resourceOfPlayer
      else resourceOfPlayer
    )
    this.copy(resourceCards = updatedResourceCards)

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
