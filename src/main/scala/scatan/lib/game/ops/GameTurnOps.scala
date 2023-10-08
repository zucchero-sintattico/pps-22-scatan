package scatan.lib.game.ops

import scatan.lib.game.Game
import scatan.lib.game.ops.TurnOps.next

/** Operations on [[Game]] related to advancing the game to the next turn.
  */
object GameTurnOps:
  extension [State, PhaseType, StepType, Action, Player](game: Game[State, PhaseType, StepType, Action, Player])

    private def nextTurnInSamePhase: Option[Game[State, PhaseType, StepType, Action, Player]] =
      for
        nextStep <- game.rules.startingSteps.get(game.gameStatus.phase)
        nextStatus = game.gameStatus.copy(
          step = nextStep
        )
        nextPlayer = game.playersIterator.next()
        nextTurn = game.turn.next(nextPlayer)
      yield game.copy(
        turn = nextTurn,
        gameStatus = nextStatus
      )

    private def nextTurnInNextPhase: Option[Game[State, PhaseType, StepType, Action, Player]] =
      for
        nextPhase <- game.rules.nextPhase.get(game.gameStatus.phase)
        initialStep <- game.rules.startingSteps.get(nextPhase)
        nextPlayersIterator <- game.rules.phaseTurnIteratorFactories.get(nextPhase).map(_.apply(game.players))
        nextPlayer <- nextPlayersIterator.nextOption()
        nextTurn = game.turn.next(nextPlayer)
      yield game.copy(
        turn = nextTurn,
        gameStatus = game.gameStatus.copy(
          phase = nextPhase,
          step = initialStep
        )
      )

    /** Advances the game to the next turn.
      * @return
      *   None if the game is over or it is not allowed to advance to the next turn in the current status, or a new game
      *   with the next turn.
      */
    private[ops] def nextTurn: Option[Game[State, PhaseType, StepType, Action, Player]] =
      if game.playersIterator.hasNext then nextTurnInSamePhase
      else nextTurnInNextPhase
