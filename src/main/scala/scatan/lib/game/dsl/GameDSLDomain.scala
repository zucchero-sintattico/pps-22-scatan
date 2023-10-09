package scatan.lib.game.dsl

private object GameDSLDomain:

  import PropertiesDSL.*

  given [State, P, S, A, Player]: Factory[GameCtx[State, P, S, A, Player]] with
    override def apply(): GameCtx[State, P, S, A, Player] = new GameCtx[State, P, S, A, Player]
  class GameCtx[State, P, S, A, Player]:
    val phases: SequenceProperty[PhaseCtx[State, P, S, A, Player]] = SequenceProperty()
    val players: OptionalProperty[PlayersCtx] = OptionalProperty()
    val winner: OptionalProperty[State => Option[Player]] = OptionalProperty()
    val initialPhase: OptionalProperty[P] = OptionalProperty()
    val stateFactory: OptionalProperty[Seq[Player] => State] = OptionalProperty()

  given Factory[PlayersCtx] with
    override def apply(): PlayersCtx = new PlayersCtx
  class PlayersCtx:
    var allowedSizes: OptionalProperty[Seq[Int]] = OptionalProperty()

  given [State, Phase, Step, Action, Player]: Factory[PhaseCtx[State, Phase, Step, Action, Player]] with
    override def apply(): PhaseCtx[State, Phase, Step, Action, Player] = new PhaseCtx[State, Phase, Step, Action, Player]
  class PhaseCtx[State, Phase, Step, Action, Player]:
    var phase: OptionalProperty[Phase] = OptionalProperty()
    var initialStep: OptionalProperty[Step] = OptionalProperty()
    var endingStep: OptionalProperty[Step] = OptionalProperty()
    var nextPhase: OptionalProperty[Phase] = OptionalProperty()
    var onEnter: OptionalProperty[State => State] = OptionalProperty()
    var step: SequenceProperty[StepCtx[Phase, Step, Action]] = SequenceProperty()
    var playerIteratorFactory: OptionalProperty[Seq[Player] => Iterator[Player]] = OptionalProperty()


  given [P, S, A]: Factory[StepCtx[P, S, A]] with
    override def apply(): StepCtx[P, S, A] = new StepCtx[P, S, A]
  class StepCtx[P, S, A]:
    var step: OptionalProperty[S] = OptionalProperty()
    var when: SequenceProperty[(A, S)] = SequenceProperty()
