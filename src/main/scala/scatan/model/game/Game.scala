package scatan.model.game

import scatan.model.game.GameRulesDSL.GameRules

import scala.language.reflectiveCalls
type BasicState = { def isOver: Boolean }
private trait StateGame[State <: BasicState]:
  def players: Seq[Player]
  def state: State
  def isOver: Boolean = state.isOver

private trait Turnable extends StateGame[?]:
  def turn: Turn[Player]
  def nextTurn: Turnable

private trait Playable[State <: BasicState, PhaseType, ActionType <: Action[State]] extends StateGame[State]:
  def phase: PhaseType
  def canPlay(action: ActionType): Boolean
  def play(action: ActionType): Playable[State, PhaseType, ActionType]

trait Game[State <: BasicState, PhaseType, ActionType <: Action[State]]
    extends StateGame[State]
    with Turnable
    with Playable[State, PhaseType, ActionType]:
  override def nextTurn: Game[State, PhaseType, ActionType]
  override def play(action: ActionType): Game[State, PhaseType, ActionType]

object Game:
  def apply[State <: BasicState, PhaseType, ActionType <: Action[State]](
      players: Seq[Player]
  )(using
      gameRulesDSL: GameRulesDSL[State, PhaseType, ActionType]
  ): Game[State, PhaseType, ActionType] =
    given gameRules: GameRules[State, PhaseType, ActionType] = gameRulesDSL.configuration
    Game[State, PhaseType, ActionType](players)(gameRules)

  def apply[State <: BasicState, PhaseType, ActionType <: Action[State]](
      players: Seq[Player]
  )(gameRules: GameRules[State, PhaseType, ActionType]): Game[State, PhaseType, ActionType] =
    require(gameRules.playersSizes contains players.size, "Invalid number of players")
    given GameRules[State, PhaseType, ActionType] = gameRules
    GameImpl(
      players,
      Turn(1, players.head),
      gameRules.initialPhase.get,
      gameRules.initialState.get
    )

private final case class GameImpl[State <: BasicState, PhaseType, ActionType <: Action[State]](
    players: Seq[Player],
    turn: Turn[Player],
    phase: PhaseType,
    state: State
)(using
    gameRules: GameRules[State, PhaseType, ActionType]
) extends Game[State, PhaseType, ActionType]:

  override def play(action: ActionType) =
    require(canPlay(action), "Invalid action: " + action)
    val newState = action(state)
    this.copy(state = newState, phase = gameRules.phasesMap(phase)(action))

  override def canPlay(action: ActionType): Boolean =
    gameRules.phasesMap(phase).isDefinedAt(action)

  override def nextTurn: Game[State, PhaseType, ActionType] =
    val nextPlayer = players((players.indexOf(turn.player) + 1) % players.size)
    this.copy(
      turn = turn.next(nextPlayer),
      phase = gameRules.initialPhase.get
    )
