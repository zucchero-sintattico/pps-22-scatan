package scatan.lib.game.dsl

object GameDSL:
  import GameDSLDomain.*
  import PropertiesDSL.{*, given}

  export ops.GameCtxOps.*
  export ops.PlayersCtxOps.*
  export ops.PhasesCtxOps.*
  export ops.PhaseCtxOps.*

  def Game[State, P, S, A, Player]: PropertyBuilder[GameCtx[State, P, S, A, Player]] = PropertyBuilder()
