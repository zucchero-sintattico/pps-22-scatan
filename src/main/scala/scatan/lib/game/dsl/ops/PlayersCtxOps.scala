package scatan.lib.game.dsl.ops

import scatan.lib.game.dsl.PropertiesDSL.*
import scatan.lib.game.dsl.GameDSLDomain.*

object PlayersCtxOps:
  def canBe: Contexted[PlayersCtx, PropertySetter[Seq[Int]]] =
    ctx ?=> ctx.allowedSizes
