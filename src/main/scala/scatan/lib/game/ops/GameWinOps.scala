package scatan.lib.game.ops

import scatan.lib.game.Game

/** Operations on [[Game]] related to winning and losing.
  */
object GameWinOps:

  import scala.language.reflectiveCalls

  extension [State, PhaseType, StepType, Action, Player](game: Game[State, PhaseType, StepType, Action, Player])

    /** Returns true if the game is over.
      * @return
      *   true if the game is over.
      */
    def isOver: Boolean =
      game.winner.isDefined

    /** Returns the winner of the game, if any.
      * @return
      *   the winner of the game, if any.
      */
    def winner: Option[Player] = game.rules.winnerFunction(game.state)
