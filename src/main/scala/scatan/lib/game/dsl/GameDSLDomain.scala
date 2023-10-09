package scatan.lib.game.dsl

private object GameDSLDomain:

  import PropertiesDSL.*

  class GameCtx[State, P, S, A, Player]:
    val phases: OptionalProperty[PhasesCtx[P, S, A]] = property
    val players: OptionalProperty[PlayersCtx] = property
    val winner: OptionalProperty[State => Boolean] = property
    val initialPhase: OptionalProperty[P] = property

  class PlayersCtx:
    var allowedSizes: OptionalProperty[Seq[Int]] = property

  class PhasesCtx[P, S, A]:
    var phase: SequenceProperty[PhaseCtx[P, S, A]] = property

  class PhaseCtx[P, S, A]:
    var phase: OptionalProperty[P] = property
    var when: SequenceProperty[(A, S)] = property
