package scatan.model.game

import cats.instances.long

import scatan.model.GameMap

final case class Player(name: String)

trait Game:
  def players: Seq[Player]
  def buildings: Buildings
  def developmentCards: DevelopmentCards
  def resourceCards: ResourceCards
  def awards: Awards
  def gameMap: GameMap
  def scores: Scores
  def build(building: Building, player: Player): Game
  def assignBuilding(building: Building, player: Player): Game
  def assignResourceCard(player: Player, resourceCard: ResourceCard): Game
  def assignDevelopmentCard(player: Player, developmentCard: DevelopmentCard): Game
  def consumeDevelopmentCard(player: Player, developmentCard: DevelopmentCard): Game
  def isThereAWinner: Boolean = scores.exists(_._2 >= 10)
  def winner: Option[Player] = if isThereAWinner then Some(scores.maxBy(_._2)._1) else None
object Game:
  /** Creates a new game with the given players The game must have 3 or 4 players The game map is created with a fixed
    * number of hexagons The buildings are empty The resource cards are empty The development cards are empty The awards
    * are empty
    *
    * @param players
    *   the players of the game
    * @return
    *   the new game
    */
  def apply(players: Seq[Player]): Game =
    if players.sizeIs < 3 || players.sizeIs > 4 then throw IllegalArgumentException("Game must have 3 or 4 players")
    else
      GameImpl(
        players,
        GameMap(2),
        Building.empty(players),
        ResourceCard.empty(players),
        DevelopmentCardsOfPlayers.empty(players),
        Award.empty()
      )

  def apply(
      players: Seq[Player],
      gameMap: GameMap,
      buildings: Buildings,
      resourceCards: ResourceCards,
      developmentCardsOfPlayers: DevelopmentCards
  ): Game =
    GameImpl(players, gameMap, buildings, resourceCards, developmentCardsOfPlayers)

private final case class GameImpl(
    players: Seq[Player],
    gameMap: GameMap,
    buildings: Buildings,
    resourceCards: ResourceCards,
    developmentCards: DevelopmentCards,
    assignedAwards: Awards = Award.empty()
) extends Game:

  def awards: Awards =
    val precedentLongestRoad = assignedAwards(Award(AwardType.LongestRoad))
    val longestRoad =
      buildings.foldLeft(precedentLongestRoad.getOrElse((Player(""), 0)))((playerWithLongestRoad, buildingsOfPlayer) =>
        val roads = buildingsOfPlayer._2.filter(_.buildingType == BuildingType.Road)
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

  override def assignBuilding(building: Building, player: Player): Game =
    this.copy(
      buildings = buildings.updated(player, buildings(player) :+ building),
      assignedAwards = awards
    )

  override def build(building: Building, player: Player): Game =
    if verifyResourceCost(player, building.buildingType.cost) then
      val remainingResourceCards = building.buildingType.cost.foldLeft(resourceCards(player))((cards, resourceCost) =>
        cards.filter(_.resourceType != resourceCost._1).drop(resourceCost._2)
      )
      val gameWithConsumedResources = this.copy(resourceCards = resourceCards.updated(player, remainingResourceCards))
      gameWithConsumedResources.assignBuilding(building, player)
    else this

  override def assignResourceCard(player: Player, resourceCard: ResourceCard): Game =
    this.copy(
      resourceCards = resourceCards.updated(player, resourceCards(player) :+ resourceCard)
    )

  override def assignDevelopmentCard(player: Player, developmentCard: DevelopmentCard): Game =
    this.copy(
      developmentCards = developmentCards.updated(player, developmentCards(player) :+ developmentCard),
      assignedAwards = awards
    )

  def consumeDevelopmentCard(player: Player, developmentCard: DevelopmentCard): Game =
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
    buildings.foldLeft(Score.empty(players))((scores, buildingsOfPlayer) =>
      scores.updated(
        buildingsOfPlayer._1,
        buildingsOfPlayer._2.foldLeft(0)((score, building) => score + buildingScore(building.buildingType))
      )
    )

  import Score.given
  import cats.syntax.semigroup.*
  def scores: Scores =
    val partialScores = Seq(partialScoresWithAwards, partialScoresWithBuildings)
    partialScores.foldLeft(Score.empty(players))(_ |+| _)
