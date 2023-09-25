package scatan.lib.game

import GameRulesDSL.GameRules

import scala.language.reflectiveCalls
type BasicState = { def isOver: Boolean; def winner: Option[Player] }
private[game] trait StateGame[State]:
  def players: Seq[Player]
  def state: State

private[game] class StateGameImpl[State](
   val players: Seq[Player],
   val state: State)
    extends StateGame[State]:
  override def equals(obj: Any): Boolean =
    obj match
      case other: StateGameImpl[State] =>
        players == other.players && state == other.state
      case _ => false
  override def hashCode(): Int =
    players.hashCode() + state.hashCode()

private[game] object StateGame:
  def apply[State](players: Seq[Player], state: State): StateGame[State] =
    require(players.nonEmpty, "Invalid number of players")
    require(state != null, "Invalid state")
    StateGameImpl(players, state)

private[game] trait Turnable extends StateGame[?]:
  def turn: Turn[Player]
  def nextTurn: Turnable

private[game] class TurnableGameImpl[State](
  players: Seq[Player],
  state: State,
  val turn: Turn[Player]
  ) extends StateGameImpl(players, state) with Turnable:
    override def nextTurn: Turnable =
      val nextPlayer = players((players.indexOf(turn.player) + 1) % players.size)
      TurnableGameImpl(
        players,
        state,
        turn.next(nextPlayer)
      )
    override def equals(obj: Any): Boolean =
      obj match
        case other: TurnableGameImpl[State] =>
          super.equals(other) && turn == other.turn
        case _ => false
    override def hashCode(): Int =
      super.hashCode() + turn.hashCode()

private[game] object Turnable:
  def apply[State](players: Seq[Player], state: State, turn: Turn[Player]): Turnable =
    require(players.contains(turn.player), "Invalid player")
    TurnableGameImpl(players, state, turn)

private trait Playable[State <: BasicState, PhaseType, ActionType <: Action[State]] extends StateGame[State]:
  def phase: PhaseType
  def canPlay(action: ActionType): Boolean
  def play(action: ActionType): Playable[State, PhaseType, ActionType]

trait Game[State <: BasicState, PhaseType, ActionType <: Action[State]]
    extends StateGame[State]
    with Turnable
    with Playable[State, PhaseType, ActionType]:
  def isOver: Boolean = state.isOver
  def winner: Option[Player] = state.winner
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
      gameRules.initialState.get(players)
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
