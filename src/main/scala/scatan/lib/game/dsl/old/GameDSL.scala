package scatan.lib.game.dsl.old

/** A type-safe DSL for defining games.
  *
  * @tparam Player
  *   The type of player in the game.
  * @tparam State
  *   The type of the game state.
  * @tparam PhaseType
  *   The type of the phase of the game.
  * @tparam StepType
  *   The type of the step of the game.
  * @tparam ActionType
  *   The type of the action of the game.
  */
trait GameDSL:
  type Player
  type State
  type PhaseType
  type StepType
  type ActionType

  private val typedDSL = new TypedGameDSL[State, PhaseType, StepType, ActionType, Player] {}

  export typedDSL.{isOver as _, winner as _, *}
