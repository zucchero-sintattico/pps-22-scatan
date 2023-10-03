package scatan.lib.game.dsl


object PlayersDSLOps:
  case class PlayersDSLContext[State, PhaseType, StepType, ActionType]()(using
      val dsl: GameDSL[State, PhaseType, StepType, ActionType]
  )

  def canBe[State, PhaseType, StepType, ActionType](playersSizes: Int*)(using
      playersDSLContext: PlayersDSLContext[State, PhaseType, StepType, ActionType]
  ): Unit =
    playersDSLContext.dsl.rules = playersDSLContext.dsl.rules.copy(playersSize = playersSizes.toSet)

  def canBe[State, PhaseType, StepType, ActionType](playersSizes: Set[Int])(using
      playersDSLContext: PlayersDSLContext[State, PhaseType, StepType, ActionType]
  ): Unit =
    playersDSLContext.dsl.rules = playersDSLContext.dsl.rules.copy(playersSize = playersSizes)

  def canBe[State, PhaseType, StepType, ActionType](playersSizes: Range)(using
      playersDSLContext: PlayersDSLContext[State, PhaseType, StepType, ActionType]
  ): Unit =
    playersDSLContext.dsl.rules = playersDSLContext.dsl.rules.copy(playersSize = playersSizes.toSet)
