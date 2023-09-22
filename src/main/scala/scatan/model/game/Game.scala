package scatan.model.game

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
final case class Turn[ActionType](
    number: Int,
    currentPlayer: Player,
    currentPhase: Phase[ActionType]
):
  if number < 1 then throw IllegalArgumentException("A turn must have a number greater than 0")
  export currentPhase.{isAllowed, allowedActions}

case class Phase[ActionType](allowedActions: Set[ActionType]):
  def isAllowed(actionType: ActionType): Boolean = allowedActions.contains(actionType)

class Action[ActionType](
    val actionType: ActionType,
    val apply: Game[ActionType] => Game[ActionType]
)

/** A Game instance
  * @param players
  *   the players of the game
  * @param currentTurn
  *   the current turn
  * @param isOver
  *   whether the game is over
  */
trait Game[ActionType]:
  def players: Seq[Player]
  def currentPlayer: Player
  def currentTurn: Turn[ActionType]
  def currentPhase: Phase[ActionType]
  def isOver: Boolean
  def isAllowed(actionType: ActionType): Boolean
  def allowedActions: Set[ActionType]
  def play[A <: Action[ActionType]](action: A): Game[ActionType]

object Game:
  private def validatePlayers(players: Seq[Player]): Unit =
    if players.sizeIs < 3 then throw IllegalArgumentException("A game must have at least 3 players")
    if players.sizeIs > 4 then throw IllegalArgumentException("A game must have at most 4 players")

  def apply[ActionType](
      players: Seq[Player]
  ): Game[ActionType] =
    Game(players, Turn(1, players.head, new Phase(Set.empty)))

  def apply[ActionType](
      players: Seq[Player],
      currentTurn: Turn[ActionType],
      isOver: Boolean = false
  ): Game[ActionType] =
    validatePlayers(players)
    GameImpl(players, currentTurn, isOver)

private final case class GameImpl[ActionType](
    players: Seq[Player],
    currentTurn: Turn[ActionType],
    isOver: Boolean
) extends Game[ActionType]:
  export currentTurn.{currentPhase, isAllowed, allowedActions}
  override def currentPlayer: Player = currentTurn.currentPlayer

  /** @param action
    *   the action to play
    * @return
    *   the game after the action is played
    */
  override def play[A <: Action[ActionType]](action: A): Game[ActionType] =
    if this.isOver then throw IllegalStateException("The game is over")
    if !this.isAllowed(action.actionType) then throw IllegalArgumentException(s"Action $action is not allowed")
    action.apply(this)
