package scatan.model

import scatan.model.Game.apply

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
  def awards: Awards
  def gameMap: GameMap
  def scores: Scores

  def assignAward(award: Award, player: Player): Game
  def assignBuilding(building: Building, player: Player): Game

object Game:
  def apply(players: Seq[Player]): Game =
    if players.size < 3 || players.size > 4 then throw IllegalArgumentException("Game must have 3 or 4 players")
    else GameImpl(players, Award.empty(), GameMap(2), Building.empty(players))

  def apply(players: Seq[Player], awards: Awards, gameMap: GameMap, buildings: Buildings): Game =
    GameImpl(players, awards, gameMap, buildings)

private final case class GameImpl(
    players: Seq[Player],
    awards: Awards,
    gameMap: GameMap,
    buildings: Buildings
) extends Game:

  override def assignAward(award: Award, player: Player): Game =
    val newAwards = awards.updated(award, Some(player))
    Game(players, newAwards, gameMap, buildings)

  override def assignBuilding(building: Building, player: Player): Game =
    val newBuildings = buildings.updated(player, buildings.get(player).get :+ building)
    Game(players, awards, gameMap, newBuildings)

  private def calculateScoreWithAwards(): Scores =
    val playersWithAwards = awards.filter(_._2.isDefined).map(_._2.get)
    playersWithAwards.foldLeft(Score.empty(players))((scores, player) => scores.updated(player, scores(player) + 1))

  private def calculateBuildingsScores(): Scores =
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

  /** The scores of the players The score is calculated by the buildings and the awards The buildings value are
    * calculated by the building type
    *
    * @return
    *   the scores of the players
    */
  def scores: Scores =
    val scoreWithBuildings = calculateBuildingsScores()
    val scoreWithAwards = calculateScoreWithAwards()
    scoreWithBuildings.map((player, score) => (player, score + scoreWithAwards(player)))
