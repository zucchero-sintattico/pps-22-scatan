package scatan.lib.game.ops

import scatan.lib.game.{NewGame, Turn}

object GameTurnOps:
  extension [State, PhaseType, StepType, Action, Player](game: NewGame[State, PhaseType, StepType, Action, Player])

    private def nextTurnInSamePhase: Option[NewGame[State, PhaseType, StepType, Action, Player]] =
      for
        nextStep <- game.rules.initialSteps.get(game.status.phase)
        nextStatus = game.status.copy(
          step = nextStep
        )
        nextPlayer = game.playersIterator.next()
        nextTurn = game.turn.next(nextPlayer)
      yield game.copy(
        turn = nextTurn,
        status = nextStatus
      )

    private def nextTurnInNextPhase: Option[NewGame[State, PhaseType, StepType, Action, Player]] =
      for
        nextPhase <- game.rules.nextPhase.get(game.status.phase)
        initialStep <- game.rules.initialSteps.get(nextPhase)
        nextPlayersIterator <- game.rules.turnIteratorFactories.get(nextPhase).map(_.apply(game.players))
        nextPlayer <- nextPlayersIterator.nextOption()
        nextTurn = game.turn.next(nextPlayer)
      yield game.copy(
        turn = nextTurn,
        status = game.status.copy(
          phase = nextPhase,
          step = initialStep
        )
      )

    /** Advances the game to the next turn.
      * @return
      *   None if the game is over or it is not allowed to advance to the next turn in the current status, or a new game
      *   with the next turn.
      */
    def nextTurn: Option[NewGame[State, PhaseType, StepType, Action, Player]] =
      if game.playersIterator.hasNext then nextTurnInSamePhase
      else nextTurnInNextPhase
