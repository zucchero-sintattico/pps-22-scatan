package scatan.lib.game

trait Turn[Player]:
  def number: Int
  def player: Player
  def next(nextPlayer: Player): Turn[Player]

object Turn:
  def apply[Player](number: Int, player: Player): Turn[Player] = TurnImpl(number, player)

private final case class TurnImpl[Player](number: Int, player: Player) extends Turn[Player]:
  require(number > 0, "Turn number must be non-negative")
  override def next(nextPlayer: Player): Turn[Player] = TurnImpl(number + 1, nextPlayer)
