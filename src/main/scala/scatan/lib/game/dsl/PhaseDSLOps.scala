package scatan.lib.game.dsl

import TurnDSLOps.TurnDSLContext
import scatan.lib.game.Action

object PhaseDSLOps:
  case class PhaseDSLContext[State, PhaseType, StepType, ActionType](phase: PhaseType)(using
      val dsl: GameDSL[State, PhaseType, StepType, ActionType]
  )

  def Turn[State, PhaseType, StepType, ActionType](
      init: TurnDSLContext[State, PhaseType, StepType, ActionType] ?=> Unit
  )(using
      phaseDSLContext: PhaseDSLContext[State, PhaseType, StepType, ActionType]
  ): Unit =
    given TurnDSLContext[State, PhaseType, StepType, ActionType] =
      TurnDSLContext[State, PhaseType, StepType, ActionType](phaseDSLContext.phase)(using phaseDSLContext.dsl)
    init

  def When[State, PhaseType, StepType, ActionType](step: StepType)(init: (ActionType, StepType)*)(using
      phaseDSLContext: PhaseDSLContext[State, PhaseType, StepType, ActionType]
  ): Unit =
    ???
