package scatan.lib.game.dsl

import scatan.model.map.GameMap

private object GameDSLDomain:

  import PropertiesDSL.*
  export Factories.given

  /** The game context is used to define the game.
    */
  case class GameCtx[State, P, S, A, Player](
      phases: MultipleProperty[PhaseCtx[State, P, S, A, Player]] = MultipleProperty[PhaseCtx[State, P, S, A, Player]](),
      players: OptionalProperty[PlayersCtx] = OptionalProperty[PlayersCtx](),
      winner: OptionalProperty[State => Option[Player]] = OptionalProperty[State => Option[Player]](),
      initialPhase: OptionalProperty[P] = OptionalProperty[P](),
      stateFactory: OptionalProperty[(GameMap, Seq[Player]) => State] =
        OptionalProperty[(GameMap, Seq[Player]) => State]()
  )

  /** The players context is used to define the players info of the game.
    */
  case class PlayersCtx(allowedSizes: OptionalProperty[Seq[Int]] = OptionalProperty[Seq[Int]]())

  /** The phase context is used to define a phase of the game.
    */
  case class PhaseCtx[State, Phase, Step, Action, Player](
      phase: OptionalProperty[Phase] = OptionalProperty[Phase](),
      initialStep: OptionalProperty[Step] = OptionalProperty[Step](),
      endingStep: OptionalProperty[Step] = OptionalProperty[Step](),
      nextPhase: OptionalProperty[Phase] = OptionalProperty[Phase](),
      onEnter: OptionalProperty[State => State] = OptionalProperty[State => State](),
      steps: MultipleProperty[StepCtx[Phase, Step, Action]] = MultipleProperty[StepCtx[Phase, Step, Action]](),
      playerIteratorFactory: OptionalProperty[Seq[Player] => Iterator[Player]] =
        OptionalProperty[Seq[Player] => Iterator[Player]]()
  )

  /** The step context is used to define a step of the game.
    */
  case class StepCtx[P, S, A](
      step: OptionalProperty[S] = OptionalProperty[S](),
      when: MultipleProperty[(A, S)] = MultipleProperty[(A, S)]()
  )

  private object Factories:

    given [State, P, S, A, Player]: Factory[GameCtx[State, P, S, A, Player]] with
      override def create(): GameCtx[State, P, S, A, Player] = new GameCtx[State, P, S, A, Player]

    given Factory[PlayersCtx] with
      override def create(): PlayersCtx = new PlayersCtx

    given [State, Phase, Step, Action, Player]: Factory[PhaseCtx[State, Phase, Step, Action, Player]] with
      override def create(): PhaseCtx[State, Phase, Step, Action, Player] = PhaseCtx()

    given [P, S, A]: Factory[StepCtx[P, S, A]] with
      override def create(): StepCtx[P, S, A] = new StepCtx[P, S, A]
