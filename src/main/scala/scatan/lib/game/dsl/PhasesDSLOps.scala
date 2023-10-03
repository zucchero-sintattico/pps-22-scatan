package scatan.lib.game.dsl

import PhaseDSLOps.PhaseDSLContext

object PhasesDSLOps:
  case class PhasesDSLContext[State, PhaseType, StepType, ActionType, Player]()(using
      val dsl: GameDSL[State, PhaseType, StepType, ActionType, Player]
  )

  def On[State, PhaseType, StepType, ActionType, Player](using
      phasesDSLContext: PhasesDSLContext[State, PhaseType, StepType, ActionType, Player]
  )(
      phase: PhaseType
  )(
      init: PhaseDSLContext[State, PhaseType, StepType, ActionType, Player] ?=> Unit
  ): Unit =
    given PhaseDSLContext[State, PhaseType, StepType, ActionType, Player] =
      PhaseDSLContext[State, PhaseType, StepType, ActionType, Player](phase)(using phasesDSLContext.dsl)
    // Create a phase rules
    init
