package scatan.lib.game.dsl

import scatan.lib.game.Player

object TurnDSLOps:
  case class TurnDSLContext[State, PhaseType, StepType, ActionType](phase: PhaseType)(using
      val dsl: GameDSL[State, PhaseType, StepType, ActionType]
  )

  def StartIn[State, PhaseType, StepType, ActionType](step: StepType)(using
      turnDSLContext: TurnDSLContext[State, PhaseType, StepType, ActionType]
  ): Unit =
    ???

  def CanEndIn[State, PhaseType, StepType, ActionType](
      step: StepType
  )(using turnDSLContext: TurnDSLContext[State, PhaseType, StepType, ActionType]): Unit =
    ???

  def Iterate[State, PhaseType, StepType, ActionType](factory: Seq[Player] => Iterator[Player])(using
      turnDSLContext: TurnDSLContext[State, PhaseType, StepType, ActionType]
  ): Unit =
    ???

  def NextPhase[State, PhaseType, StepType, ActionType](phase: PhaseType)(using
      turnDSLContext: TurnDSLContext[State, PhaseType, StepType, ActionType]
  ): Unit =
    ???

  val normal = (players: Seq[Player]) => Iterator.continually(players).flatten
  val reverse = (players: Seq[Player]) => Iterator.continually(players.reverse).flatten
  val random = (players: Seq[Player]) => Iterator.continually(scala.util.Random.shuffle(players)).flatten
  val circularWithBack = (players: Seq[Player]) => (players ++ players.reverse).iterator
