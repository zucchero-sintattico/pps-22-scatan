package scatan.lib.game.dsl

private object GameDSLDomain:

  import PropertiesDSL.*

  given [State, P, S, A, Player]: Factory[GameCtx[State, P, S, A, Player]] with
    override def apply(): GameCtx[State, P, S, A, Player] = new GameCtx[State, P, S, A, Player]
  case class GameCtx[State, P, S, A, Player](
      phases: SequenceProperty[PhaseCtx[State, P, S, A, Player]] = SequenceProperty[PhaseCtx[State, P, S, A, Player]](),
      players: OptionalProperty[PlayersCtx] = OptionalProperty[PlayersCtx](),
      winner: OptionalProperty[State => Option[Player]] = OptionalProperty[State => Option[Player]](),
      initialPhase: OptionalProperty[P] = OptionalProperty[P](),
      stateFactory: OptionalProperty[Seq[Player] => State] = OptionalProperty[Seq[Player] => State]()
  )

  given Factory[PlayersCtx] with
    override def apply(): PlayersCtx = new PlayersCtx
  case class PlayersCtx(allowedSizes: OptionalProperty[Seq[Int]] = OptionalProperty[Seq[Int]]())

  given [State, Phase, Step, Action, Player]: Factory[PhaseCtx[State, Phase, Step, Action, Player]] with
    override def apply(): PhaseCtx[State, Phase, Step, Action, Player] = PhaseCtx()

  case class PhaseCtx[State, Phase, Step, Action, Player](
      phase: OptionalProperty[Phase] = OptionalProperty[Phase](),
      initialStep: OptionalProperty[Step] = OptionalProperty[Step](),
      endingStep: OptionalProperty[Step] = OptionalProperty[Step](),
      nextPhase: OptionalProperty[Phase] = OptionalProperty[Phase](),
      onEnter: OptionalProperty[State => State] = OptionalProperty[State => State](),
      steps: SequenceProperty[StepCtx[Phase, Step, Action]] = SequenceProperty[StepCtx[Phase, Step, Action]](),
      playerIteratorFactory: OptionalProperty[Seq[Player] => Iterator[Player]] =
        OptionalProperty[Seq[Player] => Iterator[Player]]()
  )

  given [P, S, A]: Factory[StepCtx[P, S, A]] with
    override def apply(): StepCtx[P, S, A] = new StepCtx[P, S, A]
  case class StepCtx[P, S, A](
      step: OptionalProperty[S] = OptionalProperty[S](),
      when: SequenceProperty[(A, S)] = SequenceProperty[(A, S)]()
  )
