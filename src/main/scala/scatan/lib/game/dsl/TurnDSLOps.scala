package scatan.lib.game.dsl

import scatan.model.game.ScatanPlayer

object TurnDSLOps:
  case class TurnDSLContext[State, PhaseType, StepType, ActionType, Player](phase: PhaseType)(using
      val dsl: GameDSL[State, PhaseType, StepType, ActionType, Player]
  )

  def StartIn[State, PhaseType, StepType, ActionType, Player](step: StepType)(using
      turnDSLContext: TurnDSLContext[State, PhaseType, StepType, ActionType, Player]
  ): Unit =
    turnDSLContext.dsl.rules = turnDSLContext.dsl.rules.copy(
      initialSteps = turnDSLContext.dsl.rules.initialSteps.updated(turnDSLContext.phase, step)
    )

  def CanEndIn[State, PhaseType, StepType, ActionType, Player](
      step: StepType
  )(using turnDSLContext: TurnDSLContext[State, PhaseType, StepType, ActionType, Player]): Unit =
    turnDSLContext.dsl.rules = turnDSLContext.dsl.rules.copy(
      endingStatus = turnDSLContext.dsl.rules.endingStatus.updated(turnDSLContext.phase, step)
    )

  def Iterate[State, PhaseType, StepType, ActionType, Player](factory: Seq[Player] => Iterator[Player])(using
      turnDSLContext: TurnDSLContext[State, PhaseType, StepType, ActionType, Player]
  ): Unit =
    turnDSLContext.dsl.rules = turnDSLContext.dsl.rules.copy(
      turnIteratorFactories = turnDSLContext.dsl.rules.turnIteratorFactories.updated(turnDSLContext.phase, factory)
    )

  def NextPhase[State, PhaseType, StepType, ActionType, Player](phase: PhaseType)(using
      turnDSLContext: TurnDSLContext[State, PhaseType, StepType, ActionType, Player]
  ): Unit =
    turnDSLContext.dsl.rules = turnDSLContext.dsl.rules.copy(
      nextPhase = turnDSLContext.dsl.rules.nextPhase.updated(turnDSLContext.phase, phase)
    )

  val normal = (players: Seq[ScatanPlayer]) => Iterator.continually(players).flatten
  val reverse = (players: Seq[ScatanPlayer]) => Iterator.continually(players.reverse).flatten
  val random = (players: Seq[ScatanPlayer]) => Iterator.continually(scala.util.Random.shuffle(players)).flatten
  val circularWithBack = (players: Seq[ScatanPlayer]) => (players ++ players.reverse).iterator
