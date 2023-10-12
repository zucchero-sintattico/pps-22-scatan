package scatan.model.game

import scatan.lib.game.Rules
import scatan.model.game.config.{ScatanActions, ScatanPhases, ScatanPlayer, ScatanSteps}
import scatan.model.game.ops.CardOps.assignResourcesAfterInitialPlacement
import scatan.model.game.ops.ScoreOps.winner

import scala.language.postfixOps

object ScatanDSL:

  import scatan.lib.game.dsl.GameDSL.*

  private val game = Game[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer] {

    Players {
      CanBe := (3 to 4)
    }

    WinnerFunction := winner
    InitialPhase := ScatanPhases.Setup
    StateFactory := { (m, p) => ScatanState(m, p) }

    Phase {
      PhaseType := ScatanPhases.Setup
      InitialStep := ScatanSteps.SetupSettlement
      EndingStep := ScatanSteps.ChangingTurn
      NextPhase := ScatanPhases.Game
      Iterate := Iterations.OnceAndBack

      Step {
        StepType := ScatanSteps.SetupSettlement
        when := ScatanActions.AssignSettlement -> ScatanSteps.SetupRoad
      }
      Step {
        StepType := ScatanSteps.SetupRoad
        when := ScatanActions.AssignRoad -> ScatanSteps.ChangingTurn
      }

    }
    Phase {
      PhaseType := ScatanPhases.Game
      InitialStep := ScatanSteps.Starting
      EndingStep := ScatanSteps.ChangingTurn
      OnEnter := { (state: ScatanState) => state.assignResourcesAfterInitialPlacement.get }
      Iterate := Iterations.Circular

      Step {
        StepType := ScatanSteps.Starting
        when := ScatanActions.RollDice -> ScatanSteps.Playing
        when := ScatanActions.RollSeven -> ScatanSteps.PlaceRobber
        when := ScatanActions.PlayDevelopmentCard -> ScatanSteps.Starting
      }
      Step {
        StepType := ScatanSteps.PlaceRobber
        when := ScatanActions.PlaceRobber -> ScatanSteps.StealCard
      }
      Step {
        StepType := ScatanSteps.StealCard
        when := ScatanActions.StoleCard -> ScatanSteps.Playing
      }
      Step {
        StepType := ScatanSteps.Playing
        when := ScatanActions.BuildSettlement -> ScatanSteps.Playing
        when := ScatanActions.BuildRoad -> ScatanSteps.Playing
        when := ScatanActions.BuildCity -> ScatanSteps.Playing
        when := ScatanActions.BuyDevelopmentCard -> ScatanSteps.Playing
        when := ScatanActions.PlayDevelopmentCard -> ScatanSteps.Playing
        when := ScatanActions.TradeWithBank -> ScatanSteps.Playing
        when := ScatanActions.TradeWithPlayer -> ScatanSteps.Playing
        when := ScatanActions.NextTurn -> ScatanSteps.ChangingTurn
      }
    }

  }

  def rules: Rules[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer] =
    game.rules
