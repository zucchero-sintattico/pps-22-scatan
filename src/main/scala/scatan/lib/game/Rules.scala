package scatan.lib.game

final case class Rules[State, P, S, A, Player](
    initialState: State,
    initialPhase: P,
    initialSteps: Map[P, S],
    allowedPlayersSizes: Set[Int],
    allowedActions: Map[GameStatus[P, S], Seq[A]],
    turnIteratorFactories: Map[P, Seq[Player] => Iterator[Player]],
    nextPhase: Map[P, P],
    nextStep: Map[(GameStatus[P, S], A), S]
)

object Rules:
  def empty[State, P, S, A, Player]: Rules[State, P, S, A, Player] =
    Rules[State, P, S, A, Player](
      initialState = null.asInstanceOf[State],
      initialPhase = null.asInstanceOf[P],
      allowedPlayersSizes = Set.empty,
      initialSteps = Map.empty,
      allowedActions = Map.empty,
      turnIteratorFactories = Map.empty,
      nextPhase = Map.empty,
      nextStep = Map.empty
    )
