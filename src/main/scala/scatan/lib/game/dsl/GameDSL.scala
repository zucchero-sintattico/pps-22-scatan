package scatan.lib.game.dsl

import scatan.lib.game.Rules

object GameDSL:
  import GameDSLDomain.*
  import PropertiesDSL.{*, given}

  export ops.GameCtxOps.*
  export ops.PlayersCtxOps.*
  export ops.PhaseCtxOps.*
  export ops.StepCtxOps.*


  given [State, P, S, A, Player]: Factory[GameCtx[State, P, S, A, Player]] with
    def apply(): GameCtx[State, P, S, A, Player] = GameCtx()
  def Game[State, P, S, A, Player]: PropertyBuilder[GameCtx[State, P, S, A, Player]] = PropertyBuilder()

  extension [State, P, S, A, Player](game: GameCtx[State, P, S, A, Player])
    def rules: Rules[State, P, S, A, Player] = ???