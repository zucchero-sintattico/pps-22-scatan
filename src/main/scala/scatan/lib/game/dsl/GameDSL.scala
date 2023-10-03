package scatan.lib.game.dsl

import scatan.lib.game.Rules
import scatan.lib.game.dsl.PhasesDSLOps.PhasesDSLContext
import scatan.lib.game.dsl.PlayersDSLOps.PlayersDSLContext

trait GameDSL[State, PhaseType, StepType, ActionType, Player]:
  var rules: Rules[State, PhaseType, StepType, ActionType, Player] = Rules.empty

  given GameDSL[State, PhaseType, StepType, ActionType, Player] = this

  def Players(init: PlayersDSLContext[State, PhaseType, StepType, ActionType, Player] ?=> Unit): Unit =
    given PlayersDSLContext[State, PhaseType, StepType, ActionType, Player] = PlayersDSLContext()
    init

  def Phases(init: PhasesDSLContext[State, PhaseType, StepType, ActionType, Player] ?=> Unit): Unit =
    given PhasesDSLContext[State, PhaseType, StepType, ActionType, Player] =
      PhasesDSLContext[State, PhaseType, StepType, ActionType, Player]()
    init
