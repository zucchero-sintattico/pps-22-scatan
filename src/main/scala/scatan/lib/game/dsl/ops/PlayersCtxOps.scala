package scatan.lib.game.dsl.ops

import scatan.lib.game.dsl.GameDSLDomain.*
import scatan.lib.game.dsl.PropertiesDSL.*

object PlayersCtxOps:
  /** Define the allowed numbers of players in the game.
    */
  def CanBe: Contexted[PlayersCtx, PropertySetter[Seq[Int]]] =
    ctx ?=> ctx.allowedSizes
