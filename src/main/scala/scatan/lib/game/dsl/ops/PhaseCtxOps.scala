package scatan.lib.game.dsl.ops

import scatan.lib.game.dsl.GameDSLDomain.*
import scatan.lib.game.dsl.PropertiesDSL.*

object PhaseCtxOps:

  /** Define the phase type for the phase
    */
  def PhaseType[Phase]: Contexted[PhaseCtx[?, Phase, ?, ?, ?], PropertySetter[Phase]] =
    ctx ?=> ctx.phase

  /** Define the initial step for the phase
    */
  def InitialStep[Step]: Contexted[PhaseCtx[?, ?, Step, ?, ?], PropertySetter[Step]] =
    ctx ?=> ctx.initialStep

  /** Define the ending step for the phase
    */
  def EndingStep[Step]: Contexted[PhaseCtx[?, ?, Step, ?, ?], PropertySetter[Step]] =
    ctx ?=> ctx.endingStep

  /** Define the next phase for the phase
    */
  def NextPhase[Phase]: Contexted[PhaseCtx[?, Phase, ?, ?, ?], PropertySetter[Phase]] =
    ctx ?=> ctx.nextPhase

  /** Define an action to be executed when the phase is entered The action is a function that takes the current state
    * and returns the new state
    */
  def OnEnter[State]: Contexted[PhaseCtx[State, ?, ?, ?, ?], PropertySetter[State => State]] =
    ctx ?=> ctx.onEnter

  /** Define a step in the phase
    */
  def Step[Phase, StepType, Action]
      : Contexted[PhaseCtx[?, Phase, StepType, Action, ?], PropertyBuilder[StepCtx[Phase, StepType, Action]]] =
    ctx ?=> ctx.steps

  /** Possible Iterators Factory for Players
    */
  object Iterations:
    /** Iterate over the sequence only once
      */
    def Once[X]: Seq[X] => Iterator[X] = _.iterator

    /** Iterate over the sequence infinitely
      */
    def Circular[X]: Seq[X] => Iterator[X] = Iterator.continually(_).flatten

    /** Iterate over the sequence once and then back
      */
    def OnceAndBack[X]: Seq[X] => Iterator[X] = seq => (seq ++ seq.reverse).iterator

  /** Define how the players are iterated over in the phase
    */
  def Iterate[Player]: Contexted[PhaseCtx[?, ?, ?, ?, Player], PropertySetter[Seq[Player] => Iterator[Player]]] =
    ctx ?=> ctx.playerIteratorFactory
