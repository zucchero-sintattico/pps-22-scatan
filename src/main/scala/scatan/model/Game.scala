package scatan.model

/** A player.
  *
  * @param name
  *   the name of the player, must be non-empty
  */
final case class Player(name: String):
  if name.isEmpty then throw IllegalArgumentException("A player must have a non-empty name")

/** A turn.
  * @param number
  *   the number of the turn, must be greater than 0
  * @param player
  *   the player whose turn it is
  * @param phase
  *   the phase of the turn
  */
final case class Turn(number: Int, currentPlayer: Player, currentPhase: Phase = Phase.Initial):
  if number < 1 then throw IllegalArgumentException("A turn must have a number greater than 0")
  export currentPhase.{isAllowed, allowedActions}

/** A Game instance
  * @param players
  *   the players of the game
  * @param currentTurn
  *   the current turn
  * @param isOver
  *   whether the game is over
  */
trait Game:
  def players: Seq[Player]
  def currentPlayer: Player
  def currentTurn: Turn
  def currentPhase: Phase
  def isOver: Boolean
  def isAllowed(actionType: ActionType): Boolean
  def allowedActions: Set[ActionType]
  def play(action: Action): Game

object Game:
  def apply(players: Seq[Player]): Game =
    if players.sizeIs < 3 then throw IllegalArgumentException("A game must have at least 3 players")
    if players.sizeIs > 4 then throw IllegalArgumentException("A game must have at most 4 players")
    Game(players, Turn(1, players.head), false)
  def apply(players: Seq[Player], currentTurn: Turn): Game =
    if players.sizeIs < 3 then throw IllegalArgumentException("A game must have at least 3 players")
    if players.sizeIs > 4 then throw IllegalArgumentException("A game must have at most 4 players")
    Game(players, currentTurn, false)
  def apply(players: Seq[Player], currentTurn: Turn, isOver: Boolean): Game =
    if players.sizeIs < 3 then throw IllegalArgumentException("A game must have at least 3 players")
    if players.sizeIs > 4 then throw IllegalArgumentException("A game must have at most 4 players")
    Game(players, currentTurn, isOver)

private final case class GameImpl(players: Seq[Player], currentTurn: Turn, isOver: Boolean) extends Game:
  export currentTurn.{currentPhase, isAllowed, allowedActions}
  override def currentPlayer: Player = currentTurn.currentPlayer

  /** @param action
    *   the action to play
    * @return
    *   the game after the action is played
    */
  override def play(action: Action): Game =
    if this.isOver then throw IllegalStateException("The game is over")
    if !this.isAllowed(action.actionType) then throw IllegalArgumentException(s"Action $action is not allowed")
    action.apply(this)
