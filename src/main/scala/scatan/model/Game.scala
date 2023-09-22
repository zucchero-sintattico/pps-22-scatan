package scatan.model

import scatan.model.Game.apply

final case class Player(name: String)

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
    else GameImpl(players, Award.empty(), GameMap(2), Score.empty(players), Building.empty(players))

  def apply(players: Seq[Player], awards: Awards, gameMap: GameMap, scores: Scores, buildings: Buildings): Game =
    GameImpl(players, awards, gameMap, scores, buildings)

private final case class GameImpl(
    players: Seq[Player],
    awards: Awards,
    gameMap: GameMap,
    scores: Scores,
    buildings: Buildings
) extends Game:

  private def calculateScoreWithAwards(awards: Awards): Scores =
    val players = awards.filter(_._2.isDefined).map(_._2.get)
    players.foldLeft(this.scores)((scores, player) => scores.updated(player, scores.get(player).get + 1))

  override def assignAward(award: Award, player: Player): Game =
    val newAwards = awards.updated(award, Some(player))
    val newScores = calculateScoreWithAwards(newAwards)
    apply(players, newAwards, gameMap, newScores, buildings)

  override def assignBuilding(building: Building, player: Player): Game =
    val newBuildings = buildings.updated(player, buildings.get(player).get :+ building)
    apply(players, awards, gameMap, scores, newBuildings)
