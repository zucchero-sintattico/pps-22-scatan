package scatan.model

import scatan.model.Game.apply

final case class Player(name: String)

trait Game:
  def players: Seq[Player]
  def awards: Awards
  def gameMap: GameMap
  def assignAward(award: Award, player: Player): Game

object Game:

  def apply(players: Seq[Player]): Game =
    if players.size < 3 || players.size > 4 then throw IllegalArgumentException("Game must have 3 or 4 players")
    else GameImpl(players, Award.EmptyAwards(), GameMap(2))

  def apply(players: Seq[Player], awards: Awards, gameMap: GameMap): Game =
    GameImpl(players, awards, gameMap)

private final case class GameImpl(players: Seq[Player], awards: Awards, gameMap: GameMap) extends Game:
  def awards(award: Award): Option[Player] = awards.get(award).flatten

  override def assignAward(award: Award, player: Player): Game =
    val newAwards = awards.updated(award, Some(player))
    apply(players, newAwards, gameMap)
