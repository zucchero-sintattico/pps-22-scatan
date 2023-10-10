package scatan.lib.game.dsl.ops

import scatan.lib.game.dsl.GameDSLDomain.*
import scatan.lib.game.dsl.PropertiesDSL.*

object GameCtxOps:

  def WinnerFunction[State, Player]
      : Contexted[GameCtx[State, ?, ?, ?, Player], PropertySetter[State => Option[Player]]] =
    ctx ?=> ctx.winner

  def Phase[State, Phase, Step, Action, Player]: Contexted[GameCtx[State, Phase, Step, Action, Player], PropertyUpdater[
    PhaseCtx[State, Phase, Step, Action, Player]
  ]] =
    ctx ?=> ctx.phases

  def Players: Contexted[GameCtx[?, ?, ?, ?, ?], PropertyUpdater[PlayersCtx]] =
    ctx ?=> ctx.players

  def InitialPhase[Phase]: Contexted[GameCtx[?, Phase, ?, ?, ?], PropertySetter[Phase]] =
    ctx ?=> ctx.initialPhase

  def StateFactory[State, Player]: Contexted[GameCtx[State, ?, ?, ?, Player], PropertySetter[Seq[Player] => State]] =
    ctx ?=> ctx.stateFactory
