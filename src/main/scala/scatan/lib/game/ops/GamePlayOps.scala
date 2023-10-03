package scatan.lib.game.ops

import scatan.lib.game.NewGame

trait Effect[A, S]:
  def apply(state: S): Option[S]

object GamePlayOps:
  extension [State, PhaseType, StepType, Action, Player](game: NewGame[State, PhaseType, StepType, Action, Player])

    /** Get the actions allowed in the current status
      * @return
      *   the allowed actions
      */
    def allowedActions: Seq[Action] =
      game.rules.allowedActions(game.status)

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
    def play[A <: Action](
        action: A
    )(using effect: Effect[A, State]): Option[NewGame[State, PhaseType, StepType, Action, Player]] =
      for
        newState <- effect(game.state)
        newStep = game.rules.nextStep((game.status, action))
        newStatus = game.status.copy(
          step = newStep
        )
        newGame = game.copy(
          state = newState,
          status = newStatus
        )
      yield newGame
