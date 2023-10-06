package scatan.controllers.game

import scatan.lib.mvc.{BaseController, Controller}
import scatan.model.ApplicationState
import scatan.model.game.ScatanModelOps.{onError, updateGame}
import scatan.model.map.{RoadSpot, StructureSpot}
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

  override def onRoadSpot(spot: RoadSpot): Unit =
    this.model
      .updateGame(_.assignRoad(spot))
      .onError(view.error("Cannot build road here"))

  override def onStructureSpot(spot: StructureSpot): Unit =
    this.model
      .updateGame(_.assignSettlement(spot))
      .onError(view.error("Cannot build settlement here"))
