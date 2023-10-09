package scatan.lib.game.dsl.ops

import scatan.lib.game.dsl.PropertiesDSL.*
import scatan.lib.game.dsl.GameDSLDomain.*

object PhaseCtxOps:
  def PhaseType[Phase]: Contexted[PhaseCtx[?, Phase, ?, ?, ?], PropertySetter[Phase]] =
    ctx ?=> ctx.phase

  def InitialStep[Step]: Contexted[PhaseCtx[?, ?, Step, ?, ?], PropertySetter[Step]] =
    ctx ?=> ctx.initialStep

  def EndingStep[Step]: Contexted[PhaseCtx[?, ?, Step, ?, ?], PropertySetter[Step]] =
    ctx ?=> ctx.endingStep

  def NextPhase[Phase]: Contexted[PhaseCtx[?, Phase, ?, ?, ?], PropertySetter[Phase]] =
    ctx ?=> ctx.nextPhase

  def OnEnter[State]: Contexted[PhaseCtx[State, ?, ?, ?, ?], PropertySetter[State => State]] =
    ctx ?=> ctx.onEnter

  def Step[Phase, StepType, Action]: Contexted[PhaseCtx[?, Phase, StepType, Action, ?], PropertyUpdater[StepCtx[Phase, StepType, Action]]] =
    ctx ?=> ctx.step

  def Iterate[Player]: Contexted[PhaseCtx[?, ?, ?, ?, Player], PropertySetter[Seq[Player] => Iterator[Player]]] =
    ctx ?=> ctx.playerIteratorFactory