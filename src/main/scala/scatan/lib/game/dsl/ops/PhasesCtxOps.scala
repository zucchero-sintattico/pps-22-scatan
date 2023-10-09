package scatan.lib.game.dsl.ops

import scatan.lib.game.dsl.PropertiesDSL.*
import scatan.lib.game.dsl.GameDSLDomain.*

object PhasesCtxOps:
  def phase[P, S, A]: Contexted[PhasesCtx[P, S, A], PropertyUpdater[PhaseCtx[P, S, A]]] =
    ctx ?=> ctx.phase