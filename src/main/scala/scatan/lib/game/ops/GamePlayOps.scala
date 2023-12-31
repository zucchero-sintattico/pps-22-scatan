package scatan.lib.game.ops

import scatan.lib.game.Game
import scatan.lib.game.ops.GameTurnOps.nextTurn

/** An effect is a function that takes a state and returns a new state if the effect is applicable.
  * @tparam A
  *   the type of the action
  * @tparam S
  *   the type of the state
  */
trait Effect[A, S]:
  def apply(state: S): Option[S]

/** Operations on [[Game]] related to playable actions.
  */
object GamePlayOps:
  extension [State, PhaseType, StepType, Action, Player](game: Game[State, PhaseType, StepType, Action, Player])

    /** Get the actions allowed in the current status
      * @return
      *   the allowed actions
      */
    def allowedActions: Set[Action] =
      game.rules.allowedActions(game.gameStatus)

    /** Check if the current player can play the given action in this status
      * @param actionType
      *   the action to check
      * @return
      *   true if the action is allowed in this status
      */
    def canPlay(actionType: Action): Boolean =
      allowedActions.contains(actionType)

    /** Play the given action if it is allowed in this status
      * @param action
      *   the action to play
      * @return
      *   Some(newState) if the action is allowed, None otherwise
      */
    def play(
        action: Action
    )(using effect: Effect[action.type, State]): Option[Game[State, PhaseType, StepType, Action, Player]] =
      for
        _ <- canPlay(action).option
        newState <- effect(game.state)
        newStep = game.rules.nextSteps((game.gameStatus, action))
        newStatus = game.gameStatus.copy(
          step = newStep
        )
        newGame = game.copy(
          state = newState,
          gameStatus = newStatus
        )
      yield
        if newGame.rules.endingSteps(newGame.gameStatus.phase) == newGame.gameStatus.step then newGame.nextTurn.get
        else newGame

  extension (bool: Boolean)
    /** Convert a boolean to an option, returning Some(()) if true, None if false
      */
    private def option: Option[Unit] =
      if bool then Some(()) else None
