package scatan.lib.game.dsl.ops

import scatan.lib.game.dsl.PropertiesDSL.*
import scatan.lib.game.dsl.GameDSLDomain.*

object PhaseCtxOps:
  def Phase[P, S, A]: Contexted[PhaseCtx[P, S, A], PropertySetter[P]] =
    ctx ?=> ctx.phase

  def when[P, S, A]: Contexted[PhaseCtx[P, S, A], PropertySetter[(A, S)]] =
    ctx ?=> ctx.when