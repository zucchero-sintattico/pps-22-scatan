package scatan.lib.game.dsl.ops

import scatan.lib.game.dsl.GameDSLDomain.*
import scatan.lib.game.dsl.PropertiesDSL.*

object StepCtxOps:

  def StepType[Step]: Contexted[StepCtx[?, Step, ?], PropertySetter[Step]] =
    ctx ?=> ctx.step

  def when[Step, Action]: Contexted[StepCtx[?, Step, Action], PropertySetter[(Action, Step)]] =
    ctx ?=> ctx.when
    
