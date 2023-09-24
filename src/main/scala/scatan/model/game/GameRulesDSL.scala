package scatan.model.game

import scatan.model.game.GameRulesDSL.GameRules

given Conversion[GameRulesDSL[?, ?, ?], GameRules[?, ?, ?]] = _.configuration

trait GameRulesDSL[State, P, A <: Action[State]]:

  val configuration = GameRules[State, P, A]()

  def Players = new Players()
  class Players:
    def canBe(range: Range): Unit = configuration.playersSizes = range
    def canBe(size: Int): Unit = configuration.playersSizes = Seq(size)

  def Start = new Start()
  class Start:
    def withState(state: State): Unit = configuration.initialState = Some(state)
    def withPhase(phase: P): Unit = configuration.initialPhase = Some(phase)

  def When = new When()
  class When:
    def in(phase: P) = new In(phase)
    class In(_phase: P):
      def phase(mapping: PartialFunction[A, P]): Unit =
        configuration.phasesMap = configuration.phasesMap.updated(_phase, mapping)

  def Turn = new Turn()
  class Turn:
    def canEndIn(phase: P): Unit = configuration.endingPhase = Some(phase)

object GameRulesDSL:

  class GameRules[State, P, A <: Action[State]]:
    var playersSizes = Seq.empty[Int]
    var initialState: Option[State] = None
    var initialPhase: Option[P] = None
    var endingPhase: Option[P] = None
    var phasesMap: Map[P, PartialFunction[A, P]] = Map.empty

  def fromSinglePhase[State, P, A <: Action[State]](players: Range, phase: P): GameRulesDSL[State, P, A] =
    new GameRulesDSL[State, P, A]:
      import scala.language.postfixOps
      Players canBe players
      Start withPhase phase
      Turn canEndIn phase
