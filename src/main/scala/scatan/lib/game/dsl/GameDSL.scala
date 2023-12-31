package scatan.lib.game.dsl

import scatan.lib.game.{GameStatus, Rules}

object GameDSL:
  import GameDSLDomain.{*, given}
  import PropertiesDSL.{*, given}

  export ops.GameCtxOps.*
  export ops.PlayersCtxOps.*
  export ops.PhaseCtxOps.*
  export ops.StepCtxOps.*

  /** DSL for defining a game
    * @tparam State
    *   The type of the game state
    * @tparam Phase
    *   The type of the phases
    * @tparam Step
    *   The type of the steps
    * @tparam Actions
    *   The type of the actions
    * @tparam Player
    *   The type of the players
    */
  def Game[State, Phase, Step, Actions, Player]: ObjectBuilder[GameCtx[State, Phase, Step, Actions, Player]] =
    ObjectBuilder()

  extension [State, P, S, A, Player](game: GameCtx[State, P, S, A, Player])
    /** Create the rules for the game
      */
    def rules: Rules[State, P, S, A, Player] =
      val ruless: Seq[Rules[State, P, S, A, Player]] = (for
        startingPhase <- game.initialPhase
        winner <- game.winner
        playersCtx <- game.players
        allowedSizes <- playersCtx.allowedSizes
      yield
        val startingSteps: Map[P, S] = (for
          phaseCtx <- game.phases
          phase <- phaseCtx.phase
          startingStep <- phaseCtx.initialStep
        yield phase -> startingStep).toMap
        val endingSteps: Map[P, S] = (for
          phaseCtx <- game.phases
          phase <- phaseCtx.phase
          endingStep <- phaseCtx.endingStep
        yield phase -> endingStep).toMap
        val initialActions: Map[P, State => State] = (for
          phaseCtx <- game.phases
          phase <- phaseCtx.phase
          initialAction <- phaseCtx.onEnter
        yield phase -> initialAction).toMap
        val phaseTurnPlayerIteratorFactories: Map[P, Seq[Player] => Iterator[Player]] = (for
          phaseCtx <- game.phases
          phase <- phaseCtx.phase
          phaseTurnPlayerIteratorFactory <- phaseCtx.playerIteratorFactory
        yield phase -> phaseTurnPlayerIteratorFactory).toMap
        val nextPhases: Map[P, P] = (for
          phaseCtx <- game.phases
          phase <- phaseCtx.phase
          nextPhase <- phaseCtx.nextPhase
        yield phase -> nextPhase).toMap
        val actions: Map[GameStatus[P, S], Map[A, S]] = (for
          phaseCtx <- game.phases
          phase <- phaseCtx.phase
          stepCtx <- phaseCtx.steps
          step <- stepCtx.step
        yield GameStatus(phase, step) ->
          (for when <- stepCtx.when
          yield when).toMap).toMap
        Rules(
          allowedPlayersSizes = allowedSizes.toSet,
          startingPhase = startingPhase,
          startingSteps = startingSteps,
          endingSteps = endingSteps,
          winnerFunction = winner,
          initialAction = initialActions,
          phaseTurnIteratorFactories = phaseTurnPlayerIteratorFactories,
          nextPhase = nextPhases,
          actions = actions
        )
      ).toSeq
      require(ruless.sizeIs == 1, "Invalid rules")
      ruless.headOption.get
