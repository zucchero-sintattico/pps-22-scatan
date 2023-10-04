package scatan.lib.game

final case class Rules[State, P, S, A, Player](
    initialState: State,
    initialPhase: P,
    initialSteps: Map[P, S],
    actions: Map[GameStatus[P, S], Map[A, S]],
    allowedPlayersSizes: Set[Int],
    turnIteratorFactories: Map[P, Seq[Player] => Iterator[Player]],
    nextPhase: Map[P, P],
    endingSteps: Map[P, S]
):
  def allowedActions: Map[GameStatus[P, S], Seq[A]] = actions.map { case (status, actions) =>
    status -> actions.keys.toSeq
  }

  def nextStep: Map[(GameStatus[P, S], A), S] = actions.flatMap { case (status, actions) =>
    actions.map { case (action, step) =>
      (status, action) -> step
    }
  }

object Rules:
  def empty[State, P, S, A, Player]: Rules[State, P, S, A, Player] =
    Rules[State, P, S, A, Player](
      initialState = null.asInstanceOf[State],
      initialPhase = null.asInstanceOf[P],
      actions = Map.empty,
      allowedPlayersSizes = Set.empty,
      initialSteps = Map.empty,
      turnIteratorFactories = Map.empty,
      nextPhase = Map.empty,
      endingSteps = Map.empty
    )
