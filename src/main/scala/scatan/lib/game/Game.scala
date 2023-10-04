package scatan.lib.game

final case class GameStatus[P, S](phase: P, step: S)

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
    Game(
      players = players,
      state = rules.initialState,
      status = GameStatus(rules.initialPhase, rules.initialSteps(rules.initialPhase)),
      turn = Turn[Player](1, players.head),
      playersIterator = rules.turnIteratorFactories.get(rules.initialPhase).map(_(players)).getOrElse(players.iterator),
      rules = rules
    )
