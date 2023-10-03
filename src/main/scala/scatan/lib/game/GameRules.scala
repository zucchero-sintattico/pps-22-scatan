package scatan.lib.game

import scatan.lib.game
import scatan.lib.game.GamePhases.Setup
import scatan.lib.game.dsl.GameDSL
import scatan.lib.game.dsl.PlayersDSLOps.{PlayersDSLContext, canBe}
import scatan.lib.game.dsl.TurnDSLOps.{CanEndIn, Iterate, NextPhase, StartIn, TurnDSLContext, circularWithBack, normal}

import scala.language.postfixOps

final case class PhaseRules[State, PhaseType, StepType, ActionType](
    iteratorFactory: Seq[Player] => Iterator[Player],
    turnIterator: Iterator[Player],
    stepActions: Map[StepType, PartialFunction[ActionType, StepType]],
    startIn: StepType,
    canEndIn: StepType,
    nextPhase: PhaseType
)

final case class GameRules[State, PhaseType, StepType, ActionType](
    playersSize: Set[Int],
    phases: Map[PhaseType, PhaseRules[State, PhaseType, StepType, ActionType]]
)

case class MyState()
enum GamePhases:
  case Setup
  case Game

enum ScatanSteps:
  case SetupSettlement
  case SetupRoad
  case Setupped
  case Starting
  case PlaceRobber
  case StoleCard
  case Playing

enum ActionType:
  case Roll
  case RollSeven
  case PlaceRobber
  case StoleCard
  case BuyDevelopmentCard
  case PlayDevelopmentCard
  case TradeWithPlayer
  case TradeWithBank
  case BuildRoad
  case BuildSettlement
  case BuildCity

object MyDSL extends GameDSL[MyState, GamePhases, ScatanSteps, ActionType]:
  import scatan.lib.game.dsl.PhasesDSLOps.*
  import scatan.lib.game.dsl.PhaseDSLOps.*
  import ScatanSteps.*

  Players {
    canBe(3, 4)
  }

  Phases {

    On(GamePhases.Setup) {

      Turn {
        Iterate(circularWithBack)
        StartIn(SetupSettlement)
        CanEndIn(Setupped)
        NextPhase(GamePhases.Game)
      }

      When(SetupSettlement) {
        ActionType.BuildSettlement -> SetupRoad
      }

      When(SetupRoad) {
        ActionType.BuildRoad -> Setupped
      }

    }

    On(GamePhases.Game) {

      Turn {
        Iterate(normal)
        StartIn(Starting)
        CanEndIn(Playing)
      }

      When(Starting) {
        ActionType.Roll -> Playing
        ActionType.RollSeven -> PlaceRobber
        ActionType.PlayDevelopmentCard -> Starting
      }

      When(PlaceRobber) {
        ActionType.PlaceRobber -> StoleCard
      }

      When(StoleCard) {
        ActionType.StoleCard -> Playing
      }

      When(Playing) {
        ActionType.BuyDevelopmentCard -> Playing
        ActionType.PlayDevelopmentCard -> Playing
        ActionType.TradeWithPlayer -> Playing
        ActionType.TradeWithBank -> Playing
        ActionType.BuildRoad -> Playing
        ActionType.BuildSettlement -> Playing
        ActionType.BuildCity -> Playing
      }

    }
  }

@main def main(): Unit =
  println("Hello world!")
  println(MyDSL.rules)
