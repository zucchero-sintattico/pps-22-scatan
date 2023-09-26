package scatan.lib.game

import scatan.lib.game.GameRulesDSL.GameRules

import scala.language.reflectiveCalls
type BasicState = { def isOver: Boolean; def winner: Option[Player] }
private[game] trait StateGame[State]:
  def players: Seq[Player]
  def state: State

private[game] final case class StateGameImpl[State](players: Seq[Player], state: State) extends StateGame[State]:
  require(players.nonEmpty, "Invalid number of players")
  require(state != null, "Invalid state")

private[game] object StateGame:
  def apply[State](players: Seq[Player], state: State): StateGame[State] =
    StateGameImpl(players, state)

private[game] trait Turnable extends StateGame[?]:
  def turn: Turn[Player]
  def nextTurn: Turnable

private[game] final case class TurnableGameImpl[State](
    stateGame: StateGame[State],
    turn: Turn[Player]
) extends StateGame[State]
    with Turnable:
  require(players.contains(turn.player), "Invalid player in turn")
  export stateGame.*
  override def nextTurn: Turnable =
    val nextPlayer = players((players.indexOf(turn.player) + 1) % players.size)
    TurnableGameImpl(
      stateGame,
      turn.next(nextPlayer)
    )

private[game] object TurnableGame:
  def apply[State](players: Seq[Player], state: State, turn: Turn[Player]): Turnable =
    TurnableGameImpl(StateGame(players, state), turn)

private[game] trait Playable[State <: BasicState, PhaseType, ActionType <: Action[State]] extends StateGame[State]:
  def phase: PhaseType
  def canPlay(action: ActionType): Boolean
  def play(action: ActionType): Playable[State, PhaseType, ActionType]

private[game] final case class PlayableGameImpl[State <: BasicState, PhaseType, ActionType <: Action[State]](
    stateGame: StateGame[State],
    phase: PhaseType
)(using
    gameRules: GameRules[State, PhaseType, ActionType]
) extends Playable[State, PhaseType, ActionType]:
  export stateGame.*
  override def canPlay(action: ActionType): Boolean =
    gameRules.phasesMap(phase).isDefinedAt(action)
  override def play(action: ActionType): Playable[State, PhaseType, ActionType] =
    require(canPlay(action), "Invalid action: " + action)
    val newState = action(state)
    this.copy(stateGame = StateGame(players, newState), phase = gameRules.phasesMap(phase)(action))

private[game] object PlayableGame:
  def apply[State <: BasicState, PhaseType, ActionType <: Action[State]](
      players: Seq[Player],
      state: State,
      phase: PhaseType
  )(using
      gameRules: GameRules[State, PhaseType, ActionType]
  ): Playable[State, PhaseType, ActionType] =
    PlayableGameImpl(StateGame(players, state), phase)

private final case class GameImplementation[State <: BasicState, PhaseType, ActionType <: Action[State]](
    turnable: Turnable,
    playable: Playable[State, PhaseType, ActionType]
)(using gameRules: GameRules[State, PhaseType, ActionType])
    extends Game[State, PhaseType, ActionType]:
  export playable.{phase, canPlay, state, players}
  export turnable.turn

  override def nextTurn: Game[State, PhaseType, ActionType] =
    val nextPlayer = players((players.indexOf(turn.player) + 1) % players.size)
    this.copy(
      turnable = TurnableGame(
        players,
        state,
        turn.next(nextPlayer)
      ),
      playable = PlayableGame(
        players,
        state,
        gameRules.initialPhase.get
      )
    )

  override def play(action: ActionType): Game[State, PhaseType, ActionType] =
    require(canPlay(action), "Invalid action: " + action)
    val newPlayable = playable.play(action)
    this.copy(
      turnable = TurnableGame(
        newPlayable.players,
        newPlayable.state,
        turn.next(turn.player)
      ),
      playable = newPlayable
    )

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
    val state = gameRules.initialState.get(players)
    val turn = Turn(1, players.head)
    GameImpl(
      players,
      turn,
      gameRules.initialPhase.get,
      state
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
