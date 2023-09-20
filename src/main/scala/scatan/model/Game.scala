package scatan.model

final case class Player(name: String):
  if name.isEmpty then throw IllegalArgumentException("A player must have a non-empty name")

final case class Turn(number: Int, player: Player, phase: Phase = Phase.Initial):
  if number < 1 then throw IllegalArgumentException("A turn must have a number greater than 0")

private final case class Game(players: Seq[Player], currentTurn: Turn, isOver: Boolean)

object Game:
  def apply(players: Seq[Player]): Game =
    if players.sizeIs < 3 then throw IllegalArgumentException("A game must have at least 3 players")
    if players.sizeIs > 4 then throw IllegalArgumentException("A game must have at most 4 players")
    Game(players, Turn(1, players.head), false)

extension (game: Game)
  /** @return
    *   the current phase
    */
  def currentPhase: Phase = game.currentTurn.phase

  /** @return
    *   the current player
    */
  def currentPlayer: Player = game.currentTurn.player

  /** @return
    *   the possible actions for the current player
    */
  def possibleActions: Set[ActionType] = game.currentTurn.phase.allowedActions

  /** @param action
    *   the action to check
    * @return
    *   true if the action is allowed for the current player
    */
  def isAllowed(action: ActionType): Boolean = game.currentTurn.phase.isAllowed(action)

  /** @param action
    *   the action to play
    * @return
    *   the game after the action is played
    */
  def play(action: Action): Game =
    if game.isOver then throw IllegalStateException("The game is over")
    if !game.isAllowed(action.actionType) then throw IllegalArgumentException(s"Action $action is not allowed")
    action.apply(game)
