package scatan.model.game

trait GameRuleDSL[P, A <: Action]:

  class GameRuleDSLConfiguration[P, A <: Action]:
    var playersSizes = Seq.empty[Int]
    var phases: Seq[P] = Seq.empty
    var initialPhase: Option[P] = None
    var endingPhase: Option[P] = None
    var phasesMap: Map[P, PartialFunction[A, P]] = Map.empty

  val configuration = GameRuleDSLConfiguration[P, A]()

  def Players = new Players()
  class Players:
    def canBe(range: Range): Unit = configuration.playersSizes = range
    def canBe(size: Int): Unit = configuration.playersSizes = Seq(size)

  def Phases(phases: P*): Unit = this.configuration.phases = phases

  def Start = new Start()
  class Start:
    def withPhase(phase: P): Unit = configuration.initialPhase = Some(phase)

  def When = this
  def in(phase: P) = new In(phase)
  class In(_phase: P):
    def phase(mapping: PartialFunction[A, P]): Unit =
      configuration.phasesMap = configuration.phasesMap.updated(_phase, mapping)

  def Turn = new Turn()
  class Turn:
    def canEndIn(phase: P): Unit = configuration.endingPhase = Some(phase)

object GameRuleDSL:
  def fromSinglePhase[P, A <: Action](players: Range, phase: P): GameRuleDSL[P, A] = new GameRuleDSL[P, A]:
    import scala.language.postfixOps
    Players canBe players
    Start withPhase phase
    Turn canEndIn phase
