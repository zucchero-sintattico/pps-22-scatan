package scatan.lib.game.ops

import scatan.lib.game.{GameStatus, Rules}

/** Operations on [[Rules]] related to their construction.
  */
object RulesOps:
  extension [State, P, S, A, Player](rules: Rules[State, P, S, A, Player])

    /** Set the allowed number of players for this game.
      * @param sizes
      *   The allowed number of players.
      * @return
      *   A new set of rules with the allowed number of players set.
      */
    def withPlayerSizes(sizes: Set[Int]): Rules[State, P, S, A, Player] =
      rules.copy(allowedPlayersSizes = sizes)

    def withStartingStateFactory(stateFactory: Seq[Player] => State): Rules[State, P, S, A, Player] =
      rules.copy(initialStateFactory = stateFactory)

    /** Set the starting phase for this game.
      * @param phase
      *   The starting phase.
      * @return
      *   A new set of rules with the starting phase set.
      */
    def withStartingPhase(phase: P): Rules[State, P, S, A, Player] =
      rules.copy(initialPhase = phase)

    /** Set the initial step for a phase.
      * @param phase
      *   The phase
      * @param step
      *   The initial step
      * @return
      *   A new set of rules with the initial step set.
      */
    def withInitialStep(phase: P, step: S): Rules[State, P, S, A, Player] =
      rules.copy(initialSteps = rules.initialSteps + (phase -> step))

    /** Set the ending step for a phase.
      * @param phase
      *   The phase
      * @param step
      *   The ending step
      * @return
      *   A new set of rules with the ending step set.
      */
    def withEndingStep(phase: P, step: S): Rules[State, P, S, A, Player] =
      rules.copy(endingSteps = rules.endingSteps + (phase -> step))

    /** Set the turn iterator factory for a phase.
      * @param phase
      *   The phase
      * @param factory
      *   The factory
      * @return
      *   A new set of rules with the turn iterator factory set.
      */
    def withPhaseTurnIteratorFactory(
        phase: P,
        factory: Seq[Player] => Iterator[Player]
    ): Rules[State, P, S, A, Player] =
      rules.copy(turnIteratorFactories = rules.turnIteratorFactories + (phase -> factory))

    /** Set the next phase for a phase.
      * @param phase
      *   The phase
      * @param next
      *   The next phase
      * @return
      *   A new set of rules with the next phase set.
      */
    def withNextPhase(phase: P, next: P): Rules[State, P, S, A, Player] =
      rules.copy(nextPhase = rules.nextPhase + (phase -> next))

    /** Set the actions for a phase.
      * @param actions
      *   The actions
      * @return
      *   A new set of rules with the actions set.
      */
    def withActions(actions: (GameStatus[P, S], Map[A, S])): Rules[State, P, S, A, Player] =
      rules.copy(actions = rules.actions + actions)

    /** Set the winner for a state.
      * @param winner
      *   The winner function
      * @return
      *   A new set of rules with the winner function.
      */
    def withWinner(winner: State => Option[Player]): Rules[State, P, S, A, Player] =
      rules.copy(winner = winner)
