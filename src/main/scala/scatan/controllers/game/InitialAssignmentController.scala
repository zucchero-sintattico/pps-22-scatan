package scatan.controllers.game

import scatan.Pages
import scatan.model.ApplicationState
import scatan.views.game.GameView
import scatan.Pages
import scatan.lib.game.Game
import scatan.lib.mvc.{BaseController, Controller}
import scatan.model.ApplicationState
import scatan.model.game.{ScatanActions, ScatanPhases, ScatanState}
import scatan.model.map.{RoadSpot, StructureSpot}
import scatan.model.components.BuildingType
import scatan.views.game.InitialAssignmentView

trait InitialAssignmentController extends Controller[ApplicationState] with PositioningHandler

object InitialAssignmentController:
  def apply(
      requirements: Controller.Requirements[InitialAssignmentView, ApplicationState]
  ): InitialAssignmentController =
    InitialAssignmentControllerImpl(requirements)

private class InitialAssignmentControllerImpl(
    requirements: Controller.Requirements[InitialAssignmentView, ApplicationState]
) extends BaseController(requirements)
    with InitialAssignmentController:

  private var freePlacement = this.model.state.game.map(_.players.size).getOrElse(0) * 2

  override def onRoadSpot(spot: RoadSpot): Unit =
    this.model.state.game.foreach { game =>
      if game.canPlay(ScatanActions.BuildRoad(spot, game.turn.player))
      then
        val newGame =
          game
            .play(ScatanActions.BuildRoad(spot, game.turn.player))
            .nextTurn
        this.model.update(_.copy(game = Some(newGame)))
        freePlacement -= 1
        if freePlacement == 0 then
          val endTurnGame = newGame.play(ScatanActions.EndInitialAssignmentPhase)
          this.model.update(_.copy(game = Some(endTurnGame)))
          this.view.switchToGame()
      else view.error("Cannot build road")
    }

  override def onStructureSpot(spot: StructureSpot): Unit =
    this.model.state.game.foreach { game =>
      if game.canPlay(ScatanActions.BuildSettlement(spot, game.turn.player))
      then
        val newGame =
          game
            .play(ScatanActions.BuildSettlement(spot, game.turn.player))
        this.model.update(_.copy(game = Some(newGame)))
      else view.error("Cannot build settlement")
    }
