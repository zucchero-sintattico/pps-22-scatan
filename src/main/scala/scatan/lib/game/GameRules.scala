package scatan.lib.game

import scatan.lib.game
import scatan.lib.game.GamePhases.Setup
import scatan.lib.game.PhaseDSLOps.PhaseDSLContext
import scatan.lib.game.PhasesDSLOps.{On, PhasesDSLContext}
import scatan.lib.game.PlayersDSLOps.{PlayersDSLContext, canBe}
import scatan.lib.game.TurnDSLOps.{CanEndIn, Iterate, NextPhase, TurnDSLContext, circularWithBack, normal}

import scala.language.postfixOps

final case class PhaseRules[State, StepType](
    turn: Seq[Player] => Iterator[Player],
    steps: Map[StepType, PartialFunction[Action[State], StepType]],
    canEndIn: StepType
)

final case class GameRules[State, PhaseType, StepType](
    playersSize: Set[Int],
    phases: Map[PhaseType, PhaseRules[State, StepType]]
)

object PlayersDSLOps:
  case class PlayersDSLContext()(using val dsl: GameDSL[?, ?, ?])

  def canBe(playersSizes: Int*)(using playersDSLContext: PlayersDSLContext): Unit =
    playersDSLContext.dsl.rules = playersDSLContext.dsl.rules.copy(playersSize = playersSizes.toSet)

  def canBe(playersSizes: Set[Int])(using playersDSLContext: PlayersDSLContext): Unit =
    playersDSLContext.dsl.rules = playersDSLContext.dsl.rules.copy(playersSize = playersSizes)

  def canBe(playersSizes: Range)(using playersDSLContext: PlayersDSLContext): Unit =
    playersDSLContext.dsl.rules = playersDSLContext.dsl.rules.copy(playersSize = playersSizes.toSet)

object TurnDSLOps:
  case class TurnDSLContext[PhaseType](phase: PhaseType)(using val dsl: GameDSL[?, ?, ?])

  def CanEndIn[StepType](step: StepType)(using turnDSLContext: TurnDSLContext[?]): Unit =
    ???

  def Iterate(apply: Seq[Player] => Iterator[Player])(using turnDSLContext: TurnDSLContext[?]): Unit =
    ???

  def NextPhase[PhaseType](nextPhase: PhaseType)(using turnDSLContext: TurnDSLContext[?]): Unit =
    ???

  val normal = (players: Seq[Player]) => Iterator.continually(players).flatten
  val reverse = (players: Seq[Player]) => Iterator.continually(players.reverse).flatten
  val random = (players: Seq[Player]) => Iterator.continually(scala.util.Random.shuffle(players)).flatten
  val circularWithBack = (players: Seq[Player]) => (players ++ players.reverse).iterator

object PhaseDSLOps:
  case class PhaseDSLContext[State, PhaseType, StepType](phase: PhaseType)(using
      val dsl: GameDSL[State, PhaseType, StepType]
  )

  def Turn(init: TurnDSLContext[?] ?=> Unit)(using
      phaseDSLContext: PhaseDSLContext[?, ?, ?]
  ): Unit =
    given TurnDSLContext[?] = TurnDSLContext(phaseDSLContext.phase)(using phaseDSLContext.dsl)
    init

  def When[StepType](step: StepType)(init: PartialFunction[Action[?], StepType])(using
      phaseDSLContext: PhaseDSLContext[?, ?, StepType]
  ): Unit =
    ???

object PhasesDSLOps:
  case class PhasesDSLContext[State, PhaseType, StepType]()(using
      val dsl: GameDSL[State, PhaseType, StepType]
  )

  def On[State, PhaseType, StepType](using phasesDSLContext: PhasesDSLContext[State, PhaseType, StepType])(
      phase: PhaseType
  )(
      init: PhaseDSLContext[State, PhaseType, StepType] ?=> Unit
  ): Unit =
    given PhaseDSLContext[State, PhaseType, StepType] = PhaseDSLContext(phase)(using phasesDSLContext.dsl)
    init

trait GameDSL[State, PhaseType, StepType]:
  var rules: GameRules[State, PhaseType, StepType] = GameRules(Set.empty, Map.empty)

  given GameDSL[State, PhaseType, StepType] = this

  given PlayersDSLContext = PlayersDSLContext()
  def Players(init: PlayersDSLContext ?=> Unit): Unit =
    init

  given PhasesDSLContext[State, PhaseType, StepType] = PhasesDSLContext()
  def Phases(init: PhasesDSLContext[State, PhaseType, StepType] ?=> Unit): Unit =
    init

case class MyState()
enum GamePhases:
  case Setup
  case Game

enum Steps:
  case SetupSettlement
  case SetupRoad
  case Setupped
  case Roll
  case PlaceRobber
  case StoleCard
  case Playing

enum Actions extends Action[MyState](identity):
  case SetupSettlement
  case SetupRoad

  case Roll(roll: Int)
  case PlaceRobber
  case StoleCard
  case Play

object MyDSL extends GameDSL[MyState, GamePhases, Steps]:
  import PhasesDSLOps.*
  import PhaseDSLOps.*

  Players {
    canBe(3, 4)
  }

  Phases {

    On(GamePhases.Setup) {

      Turn {
        Iterate(circularWithBack)
        CanEndIn(Steps.Setupped)
        NextPhase(GamePhases.Game)
      }

      When(Steps.SetupSettlement) { case Actions.SetupSettlement =>
        Steps.SetupRoad
      }

    }

    On(GamePhases.Game) {

      Turn {
        Iterate(normal)
        CanEndIn(Steps.Playing)
      }

    }
  }
