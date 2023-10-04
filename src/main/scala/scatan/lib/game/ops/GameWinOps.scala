package scatan.lib.game.ops

import scatan.lib.game.Game

object GameWinOps:

  extension [State, PhaseType, StepType, Action, Player](game: Game[State, PhaseType, StepType, Action, Player])

    /** Returns true if the game is over.
      * @return
      *   true if the game is over.
      */
    def isOver: Boolean = false // TODO: Implement

    /** Returns the winner of the game, if any.
      * @return
      *   the winner of the game, if any.
      */
    def winner: Option[Player] = None // TODO: Implement
