package scatan.model

final case class Player(name: String)

final case class Game(players: Seq[Player]):
  if players.sizeIs < 3 then throw IllegalArgumentException("A game must have at least 3 players")
  if players.sizeIs > 4 then throw IllegalArgumentException("A game must have at most 4 players")
  def currentPlayer: Player = players.head
