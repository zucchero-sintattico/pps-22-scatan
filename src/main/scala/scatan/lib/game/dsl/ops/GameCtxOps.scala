package scatan.lib.game.dsl.ops

import scatan.lib.game.dsl.PropertiesDSL.*
import scatan.lib.game.dsl.GameDSLDomain.*

object GameCtxOps:

  def winner[S]: Contexted[GameCtx[S, ?, ?, ?, ?], PropertySetter[S => Boolean]] =
    ctx ?=> ctx.winner

  def phases[P, S, A]: Contexted[GameCtx[?, P, S, A, ?], PropertyUpdater[PhasesCtx[P, S, A]]] =
    ctx ?=> ctx.phases

  def players: Contexted[GameCtx[?, ?, ?, ?, ?], PropertyUpdater[PlayersCtx]] =
    ctx ?=> ctx.players
