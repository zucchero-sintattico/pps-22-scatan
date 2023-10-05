package scatan.lib.game

/** Represents a turn in a game.
  * @tparam Player
  *   The type of player that is taking the turn.
  */
trait Turn[Player]:
  /** The turn number.
    * @return
    *   The turn number.
    */
  def number: Int

  /** The player taking the turn.
    * @return
    *   The player taking the turn.
    */
  def player: Player

object Turn:
  /** Create a new turn.
    * @param number
    *   The turn number.
    * @param player
    *   The player taking the turn.
    * @tparam Player
    *   The type of player taking the turn.
    * @return
    *   A new turn.
    */
  def apply[Player](number: Int, player: Player): Turn[Player] = TurnImpl(number, player)

  /** Create a new turn with turn number 1.
    * @param player
    *   The player taking the turn.
    * @tparam Player
    *   The type of player taking the turn.
    * @return
    *   A new turn.
    */
  def initial[Player](player: Player): Turn[Player] = Turn(1, player)

private final case class TurnImpl[Player](number: Int, player: Player) extends Turn[Player]:
  require(number > 0, "Turn number must be non-negative")
