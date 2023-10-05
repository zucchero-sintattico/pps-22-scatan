package scatan.lib.game

/** Rules of a game.
  * @param startingStateFactory
  *   initial state of the game
  * @param startingPhase
  *   initial phase of the game
  * @param startingSteps
  *   initial steps of the game for each phase
  * @param actions
  *   actions of the game for each game status
  * @param allowedPlayersSizes
  *   allowed players sizes
  * @param phaseTurnIteratorFactories
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
    startingStateFactory: Seq[Player] => State,
    startingPhase: P,
    startingSteps: Map[P, S],
    actions: Map[GameStatus[P, S], Map[A, S]],
    allowedPlayersSizes: Set[Int],
    phaseTurnIteratorFactories: Map[P, Seq[Player] => Iterator[Player]],
    nextPhase: Map[P, P] = Map.empty[P, P],
    endingSteps: Map[P, S],
    winnerFunction: State => Option[Player] = (_: State) => None
):
  /** Returns allowed actions for each game status.
    * @return
    *   allowed actions for each game status
    */
  def allowedActions: Map[GameStatus[P, S], Set[A]] = actions.map { case (status, actions) =>
    status -> actions.keys.toSet
  }

  /** Returns next steps for each game status and action.
    * @return
    *   next steps for each game status and action
    */
  def nextSteps: Map[(GameStatus[P, S], A), S] = actions.flatMap { case (status, actions) =>
    actions.map { case (action, step) =>
      (status, action) -> step
    }
  }

object Rules:
  def empty[State, P, S, A, Player]: Rules[State, P, S, A, Player] =
    Rules[State, P, S, A, Player](
      startingStateFactory = (_: Seq[Player]) => null.asInstanceOf[State],
      startingPhase = null.asInstanceOf[P],
      actions = Map.empty,
      allowedPlayersSizes = Set.empty,
      startingSteps = Map.empty,
      phaseTurnIteratorFactories = Map.empty,
      nextPhase = Map.empty,
      endingSteps = Map.empty
    )

  def fromStateFactory[State, P, S, A, Player](
      initialStateFactory: Seq[Player] => State
  ): Rules[State, P, S, A, Player] =
    Rules[State, P, S, A, Player](
      startingStateFactory = initialStateFactory,
      startingPhase = null.asInstanceOf[P],
      actions = Map.empty,
      allowedPlayersSizes = Set.empty,
      startingSteps = Map.empty,
      phaseTurnIteratorFactories = Map.empty,
      nextPhase = Map.empty,
      endingSteps = Map.empty
    )
