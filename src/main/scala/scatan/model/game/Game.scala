package scatan.model.game

import cats.instances.long

import scatan.model.GameMap

final case class Player(name: String)

/** The game, which contains all the information about the game state
  * @param players
  *   the players in the game
  * @param buildings
  *   the buildings of the players
  * @param awards
  *   the awards of the game
  * @param gameMap
  *   the game map
  * @param scores
  *   the scores of the players
  */
trait Game:
  def players: Seq[Player]
  def buildings: Buildings
  def developmentCardsOfPlayers: DevelopmentCardsOfPlayers
  def awards: Awards
  def gameMap: GameMap
  def scores: Scores
  def assignBuilding(building: Building, player: Player): Game
  def assignDevelopmentCard(player: Player, developmentCard: DevelopmentCard): Game
  def consumeDevelopmentCard(player: Player, developmentCard: DevelopmentCard): Game
object Game:
  def apply(players: Seq[Player]): Game =
    if players.sizeIs < 3 || players.sizeIs > 4 then throw IllegalArgumentException("Game must have 3 or 4 players")
    else
      GameImpl(
        players,
        GameMap(2),
        Building.empty(players),
        DevelopmentCardsOfPlayers.empty(players),
        Award.empty()
      )

  def apply(
      players: Seq[Player],
      gameMap: GameMap,
      buildings: Buildings,
      developmentCardsOfPlayers: DevelopmentCardsOfPlayers
  ): Game =
    GameImpl(players, gameMap, buildings, developmentCardsOfPlayers)

private final case class GameImpl(
    players: Seq[Player],
    gameMap: GameMap,
    buildings: Buildings,
    developmentCardsOfPlayers: DevelopmentCardsOfPlayers,
    assignedAwards: Awards = Award.empty()
) extends Game:

  def awards: Awards =
    val precedentLongestRoad = assignedAwards(Award(AwardType.LongestRoad))
    val longestRoad =
      buildings.foldLeft(precedentLongestRoad.getOrElse((Player(""), 0)))((playerWithLongestRoad, buildingsOfPlayer) =>
        val roads = buildingsOfPlayer._2.filter(_.buildingType == BuildingType.Road)
        if roads.sizeIs >= 5 && roads.sizeIs > playerWithLongestRoad._2 then (buildingsOfPlayer._1, roads.size)
        else playerWithLongestRoad
      )
    val precedentLargestArmy = assignedAwards(Award(AwardType.LargestArmy))
    val largestArmy = developmentCardsOfPlayers.foldLeft(precedentLargestArmy.getOrElse(Player(""), 0))(
      (playerWithLargestArmy, cardsOfPlayer) =>
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

  override def assignBuilding(building: Building, player: Player): Game =
    val newBuildings = buildings.updated(player, buildings(player) :+ building)
    this.copy(players, gameMap, newBuildings, developmentCardsOfPlayers, assignedAwards = awards)

  def assignDevelopmentCard(player: Player, developmentCard: DevelopmentCard): Game =
    val newDevelopmentCardsOfPlayers =
      developmentCardsOfPlayers.updated(player, developmentCardsOfPlayers(player) :+ developmentCard)
    this.copy(developmentCardsOfPlayers = newDevelopmentCardsOfPlayers, assignedAwards = awards)

  def consumeDevelopmentCard(player: Player, developmentCard: DevelopmentCard): Game =
    val remainingMatchingCards = developmentCardsOfPlayers(player).filter(_ == developmentCard).drop(1)
    val notMatchingCards = developmentCardsOfPlayers(player).filter(_ != developmentCard)
    val newDevelopmentCardsOfPlayers =
      developmentCardsOfPlayers.updated(player, notMatchingCards ++ remainingMatchingCards)
    this.copy(players, gameMap, buildings, newDevelopmentCardsOfPlayers, assignedAwards = awards)

  private def partialScoresWithAwards(): Scores =
    val playersWithAwards = awards.filter(_._2.isDefined).map(_._2.get)
    playersWithAwards.foldLeft(Score.empty(players))((scores, playerWithCount) =>
      scores.updated(playerWithCount._1, scores(playerWithCount._1) + 1)
    )

  private def partialScoresWithBuildings(): Scores =
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

  private def combinePartialScores(scoreMaps: Seq[Scores]): Scores =
    scoreMaps.foldLeft(Score.empty(players))((scores, scoreMap) =>
      scoreMap.foldLeft(scores)((scores, score) => scores.updated(score._1, scores(score._1) + score._2))
    )

  def scores: Scores =
    this.combinePartialScores(
      Seq(
        partialScoresWithBuildings(),
        partialScoresWithAwards()
      )
    )
