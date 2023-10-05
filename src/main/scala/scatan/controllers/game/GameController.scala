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
import scatan.lib.mvc.Model
import scatan.model.game.ScatanGame
import scatan.model.game.ScatanModelOps.{updateGame, onError}

trait PositioningHandler:
  def onRoadSpot(spot: RoadSpot): Unit
  def onStructureSpot(spot: StructureSpot): Unit

trait GameController extends Controller[ApplicationState] with PositioningHandler:
  def nextTurn(): Unit
  def rollDice(): Unit

object GameController:
  def apply(requirements: Controller.Requirements[GameView, ApplicationState]): GameController =
    GameControllerImpl(requirements)

private class GameControllerImpl(requirements: Controller.Requirements[GameView, ApplicationState])
    extends BaseController(requirements)
    with GameController:

  override def nextTurn(): Unit = this.model.updateGame(_.nextTurn)

  override def rollDice(): Unit = this.model.updateGame(_.rollDice);

  override def onRoadSpot(spot: RoadSpot): Unit =
    this.model
      .updateGame(_.buildRoad(spot))
      .onError(view.error("Cannot build road here"))

  override def onStructureSpot(spot: StructureSpot): Unit =
    this.model
      .updateGame(_.buildSettlement(spot))
      .onError(view.error("Cannot build settlement here"))
