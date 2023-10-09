package scatan.lib.game

import scatan.lib.game.EmptyDomain.MyPhases
import scatan.lib.game.EmptyDomain.MyPhases.*
import scatan.lib.game.EmptyDomain.Steps.Initial
import scatan.lib.game.dsl.old.{GameDSL, PhaseDSLOps, TurnDSLOps}
import scatan.lib.game.dsl.old.PhaseDSLOps.{Turn, When}
import scatan.lib.game.dsl.old.PhasesDSLOps.On
import scatan.lib.game.dsl.old.PlayersDSLOps.canBe
import scatan.lib.game.dsl.old.TurnDSLOps.*
import scatan.lib.game.ops.Effect

object EmptyDomain:
  type EmptyDomainRules = scatan.lib.game.Rules[State, MyPhases, Steps, Actions, Player]
  case class State()
  case class Player(name: String)
  enum MyPhases:
    case Game
    case GameOver
  enum Steps:
    case Initial
    case ChangingTurn
  enum Actions:
    case StartGame
    case NotPlayableAction
    case NextTurn

  def NextTurnEffect: Effect[Actions.NextTurn.type, State] = (state: State) => Some(state)

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
          CanEndIn(ChangingTurn)
          NextPhase(GameOver)
        }

        When(Steps.Initial)(
          Actions.StartGame -> Steps.Initial,
          Actions.NextTurn -> Steps.ChangingTurn
        )

        When(Steps.ChangingTurn)()

      }

      On(GameOver) {

        Turn {
          Iterate(once)
          StartIn(Initial)
          CanEndIn(ChangingTurn)
          NextPhase(GameOver)
        }

      }
    }

  def rules = EmptyGameDSL.rules
