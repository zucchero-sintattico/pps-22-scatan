package scatan.lib.game.dsl.ops

import scatan.lib.game.dsl.GameDSLDomain.*
import scatan.lib.game.dsl.PropertiesDSL.*

object StepCtxOps:

  /** Define the type of the step
    */
  def StepType[Step]: Contexted[StepCtx[?, Step, ?], PropertySetter[Step]] =
    ctx ?=> ctx.step

  /** Define a new supported action for the step and the relative step to go to when the action is performed
    */
  def when[Step, Action]: Contexted[StepCtx[?, Step, Action], PropertySetter[(Action, Step)]] =
    ctx ?=> ctx.when
