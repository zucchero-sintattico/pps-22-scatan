package scatan.lib.game

/** Rules of a game.
  * @param initialStateFactory
  *   initial state of the game
  * @param initialPhase
  *   initial phase of the game
  * @param initialSteps
  *   initial steps of the game for each phase
  * @param actions
  *   actions of the game for each game status
  * @param allowedPlayersSizes
  *   allowed players sizes
  * @param turnIteratorFactories
  *   turn iterator factories for each phase
  * @param nextPhase
  *   next phase for each phase
  * @param endingSteps
  *   ending steps for each phase
  * @tparam State
  *   state of the game
  * @tparam P
  *   phase of the game
  * @tparam S
  *   step of the game
  * @tparam A
  *   action of the game
  * @tparam Player
  *   player of the game
  */
final case class Rules[State, P, S, A, Player](
    initialStateFactory: Seq[Player] => State,
    initialPhase: P,
    initialSteps: Map[P, S],
    actions: Map[GameStatus[P, S], Map[A, S]],
    allowedPlayersSizes: Set[Int],
    turnIteratorFactories: Map[P, Seq[Player] => Iterator[Player]],
    nextPhase: Map[P, P],
    endingSteps: Map[P, S],
    winner: State => Option[Player] = (_: State) => None
):
  /** Returns allowed actions for each game status.
    * @return
    *   allowed actions for each game status
    */
  def allowedActions: Map[GameStatus[P, S], Seq[A]] = actions.map { case (status, actions) =>
    status -> actions.keys.toSeq
  }

  /** Returns next steps for each game status and action.
    * @return
    *   next steps for each game status and action
    */
  def nextStep: Map[(GameStatus[P, S], A), S] = actions.flatMap { case (status, actions) =>
    actions.map { case (action, step) =>
      (status, action) -> step
    }
  }

object Rules:
  def empty[State, P, S, A, Player]: Rules[State, P, S, A, Player] =
    Rules[State, P, S, A, Player](
      initialStateFactory = (_: Seq[Player]) => null.asInstanceOf[State],
      initialPhase = null.asInstanceOf[P],
      actions = Map.empty,
      allowedPlayersSizes = Set.empty,
      initialSteps = Map.empty,
      turnIteratorFactories = Map.empty,
      nextPhase = Map.empty,
      endingSteps = Map.empty
    )
