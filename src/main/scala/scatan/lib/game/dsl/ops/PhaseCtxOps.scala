package scatan.lib.game.dsl.ops

import scatan.lib.game.dsl.GameDSLDomain.*
import scatan.lib.game.dsl.PropertiesDSL.*

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

  def Step[Phase, StepType, Action]
      : Contexted[PhaseCtx[?, Phase, StepType, Action, ?], PropertyUpdater[StepCtx[Phase, StepType, Action]]] =
    ctx ?=> ctx.steps

  object Iterations:
    def Once[X]: Seq[X] => Iterator[X] = _.iterator
    def Circular[X]: Seq[X] => Iterator[X] = Iterator.continually(_).flatten
    def OnceAndBack[X]: Seq[X] => Iterator[X] = seq => (seq ++ seq.reverse).iterator

  def Iterate[Player]: Contexted[PhaseCtx[?, ?, ?, ?, Player], PropertySetter[Seq[Player] => Iterator[Player]]] =
    ctx ?=> ctx.playerIteratorFactory
