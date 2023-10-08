package scatan.controllers.game

import scatan.lib.mvc.{BaseController, Controller}
import scatan.model.ApplicationState
import scatan.model.components.BuildingType
import scatan.model.game.ScatanModelOps.{onError, updateGame}
import scatan.model.map.{RoadSpot, StructureSpot}
import scatan.views.game.GameView

trait PositioningHandler:
  def onRoadSpot(spot: RoadSpot): Unit
  def onStructureSpot(spot: StructureSpot): Unit

trait GameController extends Controller[ApplicationState] with PositioningHandler:
  def rollDice(): Unit

object GameController:
  def apply(requirements: Controller.Requirements[GameView, ApplicationState]): GameController =
    GameControllerImpl(requirements)

private class GameControllerImpl(requirements: Controller.Requirements[GameView, ApplicationState])
    extends BaseController(requirements)
    with GameController:

  override def rollDice(): Unit = this.model.updateGame(_.rollDice);

  override def onRoadSpot(spot: RoadSpot): Unit =
    this.model
      .updateGame(_.buildRoad(spot))
      .onError(view.error("Cannot build road here"))

  override def onStructureSpot(spot: StructureSpot): Unit =
    val alreadyContainsSettlement = this.model.state.game
      .map(_.state)
      .map(_.assignedBuildings)
      .flatMap(_.get(spot))
      .exists(_.buildingType == BuildingType.Settlement)
    if alreadyContainsSettlement then
      this.model
        .updateGame(_.buildCity(spot))
        .onError(view.error("Cannot build city here"))
    else
      this.model
        .updateGame(_.buildSettlement(spot))
        .onError(view.error("Cannot build settlement here"))
