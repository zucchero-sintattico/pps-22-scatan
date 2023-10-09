package scatan.lib.game.dsl.old

import scatan.lib.game.ops.RulesOps.*

/** Operations for defining the rules of a turn-based game.
  */
object TurnDSLOps:
  case class TurnDSLContext[State, PhaseType, StepType, ActionType, Player](phase: PhaseType)(using
      val dsl: TypedGameDSL[State, PhaseType, StepType, ActionType, Player]
  )

  def StartIn[State, PhaseType, StepType, ActionType, Player](step: StepType)(using
      turnDSLContext: TurnDSLContext[State, PhaseType, StepType, ActionType, Player]
  ): Unit =
    turnDSLContext.dsl.rules = turnDSLContext.dsl.rules.withStartingStep(turnDSLContext.phase, step)

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

  def once[Player] = (players: Seq[Player]) => players.iterator
  def normal[Player] = (players: Seq[Player]) => Iterator.continually(players).flatten
  def reverse[Player] = (players: Seq[Player]) => Iterator.continually(players.reverse).flatten
  def random[Player] = (players: Seq[Player]) => Iterator.continually(scala.util.Random.shuffle(players)).flatten
  def circularWithBack[Player] = (players: Seq[Player]) => (players ++ players.reverse).iterator
