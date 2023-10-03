package scatan.lib.game.dsl

import PhaseDSLOps.PhaseDSLContext
import scatan.lib.game.PhaseRules

object PhasesDSLOps:
  case class PhasesDSLContext[State, PhaseType, StepType, ActionType]()(using
      val dsl: GameDSL[State, PhaseType, StepType, ActionType]
  )

  def On[State, PhaseType, StepType, ActionType](using
      phasesDSLContext: PhasesDSLContext[State, PhaseType, StepType, ActionType]
  )(
      phase: PhaseType
  )(
      init: PhaseDSLContext[State, PhaseType, StepType, ActionType] ?=> Unit
  ): Unit =
    given PhaseDSLContext[State, PhaseType, StepType, ActionType] =
      PhaseDSLContext[State, PhaseType, StepType, ActionType](phase)(using phasesDSLContext.dsl)
    // Create a phase rules
    init
