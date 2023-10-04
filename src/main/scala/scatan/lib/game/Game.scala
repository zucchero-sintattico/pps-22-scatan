package scatan.lib.game

/** A game status is a pair of phase and step.
  * @param phase
  *   the current phase
  * @param step
  *   the current step
  * @tparam P
  *   the type of the phase
  * @tparam S
  *   the type of the step
  */
final case class GameStatus[P, S](phase: P, step: S)

/** A game
  * @param players
  *   the players
  * @param state
  *   the state
  * @param turn
  *   the current turn
  * @param status
  *   the current status
  * @param playersIterator
  *   the iterator of players
  * @param rules
  *   the rules
  * @tparam State
  *   the type of the state
  * @tparam PhaseType
  *   the type of the phase
  * @tparam StepType
  *   the type of the step
  * @tparam ActionType
  *   the type of the action
  * @tparam Player
  *   the type of the player
  */
final case class Game[State, PhaseType, StepType, ActionType, Player](
    players: Seq[Player],
    state: State,
    turn: Turn[Player],
    status: GameStatus[PhaseType, StepType],
    playersIterator: Iterator[Player],
    rules: Rules[State, PhaseType, StepType, ActionType, Player]
):
  require(rules.allowedPlayersSizes.contains(players.size), s"Invalid number of players: ${players.size}")

object Game:
  def apply[State, PhaseType, StepType, ActionType, Player](
      players: Seq[Player]
  )(using
      rules: Rules[State, PhaseType, StepType, ActionType, Player]
  ): Game[State, PhaseType, StepType, ActionType, Player] =
    require(rules.allowedPlayersSizes.contains(players.size), s"Invalid number of players: ${players.size}")
    val iterator = rules.turnIteratorFactories.get(rules.initialPhase).map(_(players)).getOrElse(players.iterator)
    Game(
      players = players,
      state = rules.initialStateFactory(players),
      status = GameStatus(rules.initialPhase, rules.initialSteps(rules.initialPhase)),
      turn = Turn[Player](1, iterator.next()),
      playersIterator = iterator,
      rules = rules
    )
