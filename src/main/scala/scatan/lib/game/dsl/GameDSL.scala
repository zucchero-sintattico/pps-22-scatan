package scatan.lib.game.dsl

import scatan.lib.game.GameRules
import scatan.lib.game.dsl.PhasesDSLOps.PhasesDSLContext
import scatan.lib.game.dsl.PlayersDSLOps.PlayersDSLContext

trait GameDSL[State, PhaseType, StepType, ActionType]:
  var rules: GameRules[State, PhaseType, StepType, ActionType] = GameRules(Set.empty, Map.empty)

  given GameDSL[State, PhaseType, StepType, ActionType] = this

  def Players(init: PlayersDSLContext[State, PhaseType, StepType, ActionType] ?=> Unit): Unit =
    given PlayersDSLContext[State, PhaseType, StepType, ActionType] = PlayersDSLContext()
    init

  def Phases(init: PhasesDSLContext[State, PhaseType, StepType, ActionType] ?=> Unit): Unit =
    given PhasesDSLContext[State, PhaseType, StepType, ActionType] =
      PhasesDSLContext[State, PhaseType, StepType, ActionType]()
    init
