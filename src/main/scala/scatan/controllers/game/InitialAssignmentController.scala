package scatan.controllers.game

import scatan.Pages
import scatan.model.ApplicationState
import scatan.views.game.GameView
import scatan.Pages
import scatan.lib.game.Game
import scatan.lib.mvc.{BaseController, Controller}
import scatan.model.ApplicationState
import scatan.model.game.ScatanState
import scatan.model.map.{RoadSpot, StructureSpot}
import scatan.model.components.BuildingType
import scatan.views.game.InitialAssignmentView
import scatan.model.game.ScatanModelOps.{updateGame, onError}

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

  override def onRoadSpot(spot: RoadSpot): Unit =
    this.model
      .updateGame(_.buildRoad(spot))
      .onError(view.error("Cannot build road here"))

  override def onStructureSpot(spot: StructureSpot): Unit =
    this.model
      .updateGame(_.buildSettlement(spot))
      .onError(view.error("Cannot build settlement here"))
