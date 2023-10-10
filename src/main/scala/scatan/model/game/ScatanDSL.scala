package scatan.model.game

import scatan.lib.game.dsl.PhaseDSLOps.*
import scatan.lib.game.dsl.PhasesDSLOps.*
import scatan.lib.game.dsl.PlayersDSLOps.*
import scatan.lib.game.dsl.TurnDSLOps.*
import scatan.lib.game.dsl.{GameDSL, PhaseDSLOps, PhasesDSLOps}
import scatan.model.game.config.{ScatanActions, ScatanPhases, ScatanPlayer, ScatanSteps}
import scatan.model.game.ops.CardOps.assignResourcesAfterInitialPlacement
import scatan.model.game.ops.ScoreOps.*

import scala.language.postfixOps

object ScatanDSL extends GameDSL:
  override type Player = ScatanPlayer
  override type State = ScatanState
  override type PhaseType = ScatanPhases
  override type StepType = ScatanSteps
  override type ActionType = ScatanActions

  import scatan.model.game.config.ScatanSteps.*

  Players {
    canBe(3 to 4)
  }

  StartWithStateFactory(ScatanState(_))
  StartWithPhase(ScatanPhases.Setup)

  Winner(_.winner)

  Phases {
    On(ScatanPhases.Setup) {

      Turn {
        Iterate(circularWithBack)
        StartIn(SetupSettlement)
        CanEndIn(ChangingTurn)
        NextPhase(ScatanPhases.Game)
      }

      When(SetupSettlement)(
        ScatanActions.AssignSettlement -> SetupRoad
      )

      When(SetupRoad)(
        ScatanActions.AssignRoad -> ChangingTurn
      )

      When(ChangingTurn)()

    }

    On(ScatanPhases.Game) {

      OnEnter((state: ScatanState) => state.assignResourcesAfterInitialPlacement.get)

      Turn {
        Iterate(normal)
        StartIn(Starting)
        CanEndIn(ChangingTurn)
      }

      When(Starting)(
        ScatanActions.RollDice -> Playing,
        ScatanActions.RollSeven -> PlaceRobber,
        ScatanActions.PlayDevelopmentCard -> Starting
      )

      When(PlaceRobber)(
        ScatanActions.PlaceRobber -> StealCard
      )

      When(StealCard)(
        ScatanActions.StealCard -> Playing
      )

      When(Playing)(
        ScatanActions.BuildSettlement -> Playing,
        ScatanActions.BuildRoad -> Playing,
        ScatanActions.BuildCity -> Playing,
        ScatanActions.BuyDevelopmentCard -> Playing,
        ScatanActions.PlayDevelopmentCard -> Playing,
        ScatanActions.TradeWithBank -> Playing,
        ScatanActions.TradeWithPlayer -> Playing,
        ScatanActions.NextTurn -> ChangingTurn
      )

      When(ChangingTurn)()
    }
  }
