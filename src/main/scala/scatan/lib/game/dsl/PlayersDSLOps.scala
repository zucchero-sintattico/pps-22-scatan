package scatan.lib.game.dsl

import scatan.lib.game.ops.RulesOps.withPlayerSizes

object PlayersDSLOps:
  case class PlayersDSLContext[State, PhaseType, StepType, ActionType, Player]()(using
      val dsl: TypedGameDSL[State, PhaseType, StepType, ActionType, Player]
  )

  def canBe[State, PhaseType, StepType, ActionType, Player](sizes: Set[Int])(using
      playersDSLContext: PlayersDSLContext[State, PhaseType, StepType, ActionType, Player]
  ): Unit =
    playersDSLContext.dsl.rules = playersDSLContext.dsl.rules.withPlayerSizes(sizes)

  def canBe[State, PhaseType, StepType, ActionType, Player](sizes: Int*)(using
      playersDSLContext: PlayersDSLContext[State, PhaseType, StepType, ActionType, Player]
  ): Unit =
    canBe(sizes.toSet)

  def canBe[State, PhaseType, StepType, ActionType, Player](sizes: Range)(using
      playersDSLContext: PlayersDSLContext[State, PhaseType, StepType, ActionType, Player]
  ): Unit =
    canBe(sizes.toSet)
