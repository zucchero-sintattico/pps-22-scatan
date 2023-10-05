package scatan.lib.game.dsl

import scatan.lib.game.GameStatus
import scatan.lib.game.dsl.TurnDSLOps.TurnDSLContext
import scatan.lib.game.ops.RulesOps.withActions

/** Operations for defining phases and steps in a game.
  */
object PhaseDSLOps:
  case class PhaseDSLContext[State, PhaseType, StepType, ActionType, Player](phase: PhaseType)(using
      val dsl: TypedGameDSL[State, PhaseType, StepType, ActionType, Player]
  ):
    def addStepToPhase(phase: PhaseType, step: StepType, actions: Map[ActionType, StepType]): Unit =
      val status = GameStatus(phase, step)
      dsl.rules = dsl.rules.withActions(status, actions)

  def Turn[State, PhaseType, StepType, ActionType, Player](
      init: TurnDSLContext[State, PhaseType, StepType, ActionType, Player] ?=> Unit
  )(using
      phaseDSLContext: PhaseDSLContext[State, PhaseType, StepType, ActionType, Player]
  ): Unit =
    given TurnDSLContext[State, PhaseType, StepType, ActionType, Player] =
      TurnDSLContext[State, PhaseType, StepType, ActionType, Player](phaseDSLContext.phase)(using phaseDSLContext.dsl)
    init

  def When[State, PhaseType, StepType, ActionType, Player](step: StepType)(init: (ActionType, StepType)*)(using
      phaseDSLContext: PhaseDSLContext[State, PhaseType, StepType, ActionType, Player]
  ): Unit =
    phaseDSLContext.addStepToPhase(phaseDSLContext.phase, step, init.toMap)
