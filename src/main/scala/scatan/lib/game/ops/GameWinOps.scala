package scatan.lib.game.ops

import scatan.lib.game.Game

object GameWinOps:

  extension [State, PhaseType, StepType, Action, Player](game: Game[State, PhaseType, StepType, Action, Player])
    def isOver: Boolean = ???
    def winner: Option[Player] = ???
