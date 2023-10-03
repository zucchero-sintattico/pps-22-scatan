package scatan.lib.game

case class GameStatus[P, S](phase: P, step: S)

case class Rules[P, S, A, Player](
    allowedPlayersSizes: Seq[Int],
    initialSteps: Map[P, S],
    allowedActions: Map[GameStatus[P, S], Seq[A]],
    turnIteratorFactories: Map[P, Seq[Player] => Iterator[Player]],
    nextPhase: Map[P, P],
    nextStep: Map[(GameStatus[P, S], A), S]
)

final case class NewGame[State, PhaseType, StepType, ActionType, Player](
    players: Seq[Player],
    state: State,
    turn: Turn[Player],
    status: GameStatus[PhaseType, StepType],
    playersIterator: Iterator[Player],
    rules: Rules[PhaseType, StepType, ActionType, Player]
)
