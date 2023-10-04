package scatan.lib.game.dsl

import scatan.lib.game.Rules
import scatan.lib.game.dsl.PhasesDSLOps.PhasesDSLContext
import scatan.lib.game.dsl.PlayersDSLOps.PlayersDSLContext
import scatan.lib.game.ops.RulesOps.*

/** A DSL for defining a game.
  * @tparam State
  *   The type of the game state.
  * @tparam PhaseType
  *   The type of the game phase.
  * @tparam StepType
  *   The type of the game step.
  * @tparam ActionType
  *   The type of the game action.
  * @tparam Player
  *   The type of the player.
  */
trait TypedGameDSL[State, PhaseType, StepType, ActionType, Player]:
  var rules: Rules[State, PhaseType, StepType, ActionType, Player] = Rules.empty
  given TypedGameDSL[State, PhaseType, StepType, ActionType, Player] = this

  def Players(init: PlayersDSLContext[State, PhaseType, StepType, ActionType, Player] ?=> Unit): Unit =
    given PlayersDSLContext[State, PhaseType, StepType, ActionType, Player] = PlayersDSLContext()
    init

  def Phases(init: PhasesDSLContext[State, PhaseType, StepType, ActionType, Player] ?=> Unit): Unit =
    given PhasesDSLContext[State, PhaseType, StepType, ActionType, Player] =
      PhasesDSLContext[State, PhaseType, StepType, ActionType, Player]()
    init

  def Winner(winner: State => Option[Player]): Unit =
    rules = rules.withWinner(winner)

  def StartWithPhase(phase: PhaseType): Unit =
    rules = rules.withStartingPhase(phase)

  def StartWithStateFactory(stateFactory: Seq[Player] => State): Unit =
    rules = rules.withStartingStateFactory(stateFactory)
