package scatan.lib.game.dsl.ops

import scatan.lib.game.dsl.GameDSLDomain.*
import scatan.lib.game.dsl.PropertiesDSL.*

object PlayersCtxOps:
  def CanBe: Contexted[PlayersCtx, PropertySetter[Seq[Int]]] =
    ctx ?=> ctx.allowedSizes
