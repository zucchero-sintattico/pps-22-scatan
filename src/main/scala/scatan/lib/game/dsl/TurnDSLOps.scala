package scatan.lib.game.dsl

import scatan.lib.game.ops.RulesOps.*
import scatan.model.game.ScatanPlayer

object TurnDSLOps:
  case class TurnDSLContext[State, PhaseType, StepType, ActionType, Player](phase: PhaseType)(using
      val dsl: TypedGameDSL[State, PhaseType, StepType, ActionType, Player]
  )

  def StartIn[State, PhaseType, StepType, ActionType, Player](step: StepType)(using
      turnDSLContext: TurnDSLContext[State, PhaseType, StepType, ActionType, Player]
  ): Unit =
    turnDSLContext.dsl.rules = turnDSLContext.dsl.rules.withInitialStep(turnDSLContext.phase, step)

  def CanEndIn[State, PhaseType, StepType, ActionType, Player](
      step: StepType
  )(using turnDSLContext: TurnDSLContext[State, PhaseType, StepType, ActionType, Player]): Unit =
    turnDSLContext.dsl.rules = turnDSLContext.dsl.rules.withEndingStep(turnDSLContext.phase, step)

  def Iterate[State, PhaseType, StepType, ActionType, Player](factory: Seq[Player] => Iterator[Player])(using
      turnDSLContext: TurnDSLContext[State, PhaseType, StepType, ActionType, Player]
  ): Unit =
    turnDSLContext.dsl.rules = turnDSLContext.dsl.rules.withPhaseTurnIteratorFactory(turnDSLContext.phase, factory)

  def NextPhase[State, PhaseType, StepType, ActionType, Player](phase: PhaseType)(using
      turnDSLContext: TurnDSLContext[State, PhaseType, StepType, ActionType, Player]
  ): Unit =
    turnDSLContext.dsl.rules = turnDSLContext.dsl.rules.withNextPhase(turnDSLContext.phase, phase)

  val normal = (players: Seq[ScatanPlayer]) => Iterator.continually(players).flatten
  val reverse = (players: Seq[ScatanPlayer]) => Iterator.continually(players.reverse).flatten
  val random = (players: Seq[ScatanPlayer]) => Iterator.continually(scala.util.Random.shuffle(players)).flatten
  val circularWithBack = (players: Seq[ScatanPlayer]) => (players ++ players.reverse).iterator
