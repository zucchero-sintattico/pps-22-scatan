package scatan.lib.game.dsl.ops

import scatan.lib.game.dsl.GameDSLDomain.*
import scatan.lib.game.dsl.PropertiesDSL.*
import scatan.model.map.GameMap

object GameCtxOps:

  /** Define the winner function, which is a function that takes a game state and returns the winner of the game, if
    * any.
    */
  def WinnerFunction[State, Player]
      : Contexted[GameCtx[State, ?, ?, ?, Player], PropertySetter[State => Option[Player]]] =
    ctx ?=> ctx.winner

  /** Define a phase of the game.
    */
  def Phase[State, Phase, Step, Action, Player]: Contexted[GameCtx[State, Phase, Step, Action, Player], PropertyBuilder[
    PhaseCtx[State, Phase, Step, Action, Player]
  ]] =
    ctx ?=> ctx.phases

  /** Define the Players info of the game.
    */
  def Players: Contexted[GameCtx[?, ?, ?, ?, ?], PropertyBuilder[PlayersCtx]] =
    ctx ?=> ctx.players

  /** Define the initial phase of the game.
    */
  def InitialPhase[Phase]: Contexted[GameCtx[?, Phase, ?, ?, ?], PropertySetter[Phase]] =
    ctx ?=> ctx.initialPhase

  /** Define the initial state factory of the game.
    */
  def StateFactory[State, Player]
      : Contexted[GameCtx[State, ?, ?, ?, Player], PropertySetter[(GameMap, Seq[Player]) => State]] =
    ctx ?=> ctx.stateFactory
