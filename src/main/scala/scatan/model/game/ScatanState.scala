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
  def emptySpot: Seq[Spot]
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
  /** Creates a new game with the given players The game must have 3 or 4 players The game map is created with a fixed
    * number of hexagons The buildings are empty The resource cards are empty The development cards are empty The awards
    * are empty
    *
    * @param players
    *   the players of the game
    * @return
    *   the new game
    */
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

  def emptySpot: Seq[Spot] =
    Seq(gameMap.nodes, gameMap.edges).flatten
      .filter(!assignedBuildings.isDefinedAt(_))

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

  private def verifyResourceCost(player: Player, cost: Cost): Boolean =
    cost.foldLeft(true)((result, resourceCost) =>
      result && resourceCards(player).count(_.resourceType == resourceCost._1) >= resourceCost._2
    )

  override def assignBuilding(spot: Spot, buildingType: BuildingType, player: Player): ScatanState =
    this.copy(
      assignedBuildings = assignedBuildings + AssignmentFactory(spot, player, buildingType),
      assignedAwards = awards
    )

  override def build(position: Spot, buildingType: BuildingType, player: Player): ScatanState =
    if verifyResourceCost(player, buildingType.cost) then
      val remainingResourceCards = buildingType.cost.foldLeft(resourceCards(player))((cards, resourceCost) =>
        cards.filter(_.resourceType != resourceCost._1).drop(resourceCost._2)
      )
      val gameWithConsumedResources = this.copy(resourceCards = resourceCards.updated(player, remainingResourceCards))
      gameWithConsumedResources.assignBuilding(position, buildingType, player)
    else this

  override def assignResourceCard(player: Player, resourceCard: ResourceCard): ScatanState =
    this.copy(
      resourceCards = resourceCards.updated(player, resourceCards(player) :+ resourceCard)
    )

  override def assignDevelopmentCard(player: Player, developmentCard: DevelopmentCard): ScatanState =
    this.copy(
      developmentCards = developmentCards.updated(player, developmentCards(player) :+ developmentCard),
      assignedAwards = awards
    )

  def consumeDevelopmentCard(player: Player, developmentCard: DevelopmentCard): ScatanState =
    val remainingCards = developmentCards(player).filter(_.developmentType == developmentCard.developmentType).drop(1)
    this.copy(developmentCards = developmentCards.updated(player, remainingCards), assignedAwards = awards)

  private def partialScoresWithAwards: Scores =
    val playersWithAwards = awards.filter(_._2.isDefined).map(_._2.get)
    playersWithAwards.foldLeft(Score.empty(players))((scores, playerWithCount) =>
      scores.updated(playerWithCount._1, scores(playerWithCount._1) + 1)
    )

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

  import cats.syntax.semigroup.*
  import scatan.model.components.Score.given
  def scores: Scores =
    val partialScores = Seq(partialScoresWithAwards, partialScoresWithBuildings)
    partialScores.foldLeft(Score.empty(players))(_ |+| _)

  private def getSpotsWithTileContentFromHexagons(
      hexagons: Map[Hexagon, TileContent]
  ): Map[StructureSpot, TileContent] =
    hexagons.foldLeft(Map.empty[StructureSpot, TileContent])((spotsWithTileContent, hexagonWithTile) =>
      val spotWithTile = (gameMap.nodes.filter(_.contains(hexagonWithTile._1)).head, hexagonWithTile._2)
      spotsWithTileContent.updated(spotWithTile._1, spotWithTile._2)
    )
  def assignResourceFromHexagons(hexagonsWithTileContent: Map[Hexagon, TileContent]) =
    val assignedSpotsWithTileContent =
      this
        .getSpotsWithTileContentFromHexagons(hexagonsWithTileContent)
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

  def assignResourcesFromNumber(number: Int): ScatanState =
    val hexagonsFilteredByNumber = gameMap.toContent
      .filter(
        (
            hexagon,
            tileContent
        ) => tileContent.number.isDefined && tileContent.number.get == number && hexagon != robberPlacement
      )
    assignResourceFromHexagons(hexagonsFilteredByNumber)

  def moveRobber(hexagon: Hexagon): ScatanState = this.copy(robberPlacement = hexagon)
