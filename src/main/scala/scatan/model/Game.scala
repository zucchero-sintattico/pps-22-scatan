package scatan.model

final case class Player(name: String)

final case class Turn(number: Int, player: Player):
  if number < 1 then throw IllegalArgumentException("A turn must have a number greater than 0")

private final case class Game(players: Seq[Player], currentTurn: Turn, isOver: Boolean)

object Game:
  def apply(players: Seq[Player]): Game =
    if players.sizeIs < 3 then throw IllegalArgumentException("A game must have at least 3 players")
    if players.sizeIs > 4 then throw IllegalArgumentException("A game must have at most 4 players")
    Game(players, Turn(1, players.head), false)

extension (game: Game)
  def nextTurn: Game =
    if game.isOver then throw IllegalStateException("The game is over")
    game.copy(
      players = game.players.tail :+ game.players.head,
      currentTurn = Turn(game.currentTurn.number + 1, game.players.tail.head)
    )
  def currentPlayer: Player = game.currentTurn.player
