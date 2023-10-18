package scatan.lib.game

import scatan.lib.game.dsl.GameDSL
import scatan.lib.game.dsl.GameDSL.rules
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

  def rules = game.rules

  import GameDSL.*
  private val game = Game[State, MyPhases, Steps, Actions, Player] {
    Players {
      CanBe := 2 to 4
    }

    InitialPhase := MyPhases.Game
    WinnerFunction := (_ => None)

    Phase {
      PhaseType := MyPhases.Game
      InitialStep := Steps.Initial
      EndingStep := Steps.ChangingTurn
      NextPhase := MyPhases.GameOver
      Iterate := Iterations.Once

      Step {
        StepType := Steps.Initial
        when := Actions.StartGame -> Steps.Initial
        when := Actions.NextTurn -> Steps.ChangingTurn
      }

      Step {
        StepType := Steps.ChangingTurn
      }

    }

    Phase {
      PhaseType := MyPhases.GameOver
      InitialStep := Steps.Initial
      EndingStep := Steps.ChangingTurn
      NextPhase := MyPhases.GameOver
      Iterate := Iterations.Once
    }
  }
