package scatan.model.game

trait Turn:
  def number: Int
  def player: Player

object Turn:
  def apply(number: Int, player: Player): Turn = TurnImpl(number, player)

private final case class TurnImpl(number: Int, player: Player) extends Turn:
  require(number > 0, "Turn number must be non-negative")

extension (turn: Turn)
  def next(players: Seq[Player]): Turn =
    val nextPlayerIndex = players.indexOf(turn.player) + 1
    val nextPlayer = players(nextPlayerIndex % players.size)
    Turn(turn.number + 1, nextPlayer)
