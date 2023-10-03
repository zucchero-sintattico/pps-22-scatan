package scatan.lib.game.dsl

object PlayersDSLOps:
  case class PlayersDSLContext[State, PhaseType, StepType, ActionType, Player]()(using
      val dsl: GameDSL[State, PhaseType, StepType, ActionType, Player]
  )

  def canBe[State, PhaseType, StepType, ActionType, Player](sizes: Set[Int])(using
      playersDSLContext: PlayersDSLContext[State, PhaseType, StepType, ActionType, Player]
  ): Unit =
    playersDSLContext.dsl.rules = playersDSLContext.dsl.rules.copy(allowedPlayersSizes = sizes)

  def canBe[State, PhaseType, StepType, ActionType, Player](sizes: Int*)(using
      playersDSLContext: PlayersDSLContext[State, PhaseType, StepType, ActionType, Player]
  ): Unit =
    canBe(sizes.toSet)

  def canBe[State, PhaseType, StepType, ActionType, Player](sizes: Range)(using
      playersDSLContext: PlayersDSLContext[State, PhaseType, StepType, ActionType, Player]
  ): Unit =
    canBe(sizes.toSet)
