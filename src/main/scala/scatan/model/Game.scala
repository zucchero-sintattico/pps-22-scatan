package scatan.model

import scatan.model.Game.apply

final case class Player(name: String)

trait Game:
  def players: Seq[Player]
  def awards: Awards
  def gameMap: GameMap
  def scores: Scores

  def assignAward(award: Award, player: Player): Game

object Game:
  def apply(players: Seq[Player]): Game =
    if players.size < 3 || players.size > 4 then throw IllegalArgumentException("Game must have 3 or 4 players")
    else GameImpl(players, Award.EmptyAwards(), GameMap(2), Score.EmptyScores(players))

  def apply(players: Seq[Player], awards: Awards, gameMap: GameMap, scores: Scores): Game =
    GameImpl(players, awards, gameMap, scores)

private final case class GameImpl(players: Seq[Player], awards: Awards, gameMap: GameMap, scores: Scores) extends Game:
  def awards(award: Award): Option[Player] = awards.get(award).flatten

  private def calculateScores(awards: Awards): Scores =
    val players = awards.filter(_._2.isDefined).map(_._2.get)
    players.foldLeft(this.scores)((scores, player) => scores.updated(player, scores.get(player).get + 1))

  override def assignAward(award: Award, player: Player): Game =
    val newAwards = awards.updated(award, Some(player))
    val newScores = calculateScores(newAwards)
    apply(players, newAwards, gameMap, newScores)
