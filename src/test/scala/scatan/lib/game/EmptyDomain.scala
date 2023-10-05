package scatan.lib.game

import scatan.lib.game.EmptyDomain.MyPhases
import scatan.lib.game.EmptyDomain.MyPhases.*
import scatan.lib.game.EmptyDomain.Steps.Initial
import scatan.lib.game.dsl.PhaseDSLOps.{Turn, When}
import scatan.lib.game.dsl.PhasesDSLOps.On
import scatan.lib.game.dsl.PlayersDSLOps.canBe
import scatan.lib.game.dsl.TurnDSLOps.*
import scatan.lib.game.dsl.{GameDSL, PhaseDSLOps, TurnDSLOps}

object EmptyDomain:
  type EmptyDomainRules = scatan.lib.game.Rules[State, MyPhases, Steps, Actions, Player]
  case class State()
  case class Player(name: String)
  enum MyPhases:
    case Game
    case GameOver
  enum Steps:
    case Initial
  enum Actions:
    case StartGame
    case NotPlayableAction

  object EmptyGameDSL extends GameDSL:
    override type State = EmptyDomain.State
    override type PhaseType = EmptyDomain.MyPhases
    override type StepType = EmptyDomain.Steps
    override type ActionType = EmptyDomain.Actions
    override type Player = EmptyDomain.Player

    import Steps.*

    import scala.language.postfixOps

    Players {
      canBe(2 to 4)
    }

    StartWithStateFactory((_) => State())
    StartWithPhase(Game)
    Winner(_ => None)

    Phases {
      On(Game) {

        Turn {
          Iterate(once)
          StartIn(Initial)
          CanEndIn(Initial)
          NextPhase(GameOver)
        }

        When(Steps.Initial) {
          Actions.StartGame -> Steps.Initial
        }

      }

      On(GameOver) {

        Turn {
          Iterate(once)
          StartIn(Initial)
          CanEndIn(Initial)
          NextPhase(GameOver)
        }

      }
    }

  def rules = EmptyGameDSL.rules
