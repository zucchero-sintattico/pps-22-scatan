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

  override def nextTurn(): Unit =
    this.model.state.game match
      case Some(game) =>
        if game.phase == ScatanPhases.Playing
        then this.model.update(_.copy(game = Some(game.nextTurn)))
        else view.error("Cannot end turn")
      case None =>
        ()

  override def rollDice(): Unit =
    this.model.state.game match
      case Some(game) =>
        if game.canPlay(ScatanActions.RollDice(1))
        then
          val newGame = game.play(ScatanActions.RollDice(1))
          this.model.update(_.copy(game = Some(newGame)))
        else view.error("Cannot roll dice")
      case None =>
        ()

  override def onRoadSpot(spot: RoadSpot): Unit =
    this.model.state.game match
      case Some(game) =>
        if game.canPlay(ScatanActions.BuildRoad(spot, game.turn.player))
        then
          val newGame =
            game
              .play(ScatanActions.BuildRoad(spot, game.turn.player))
          this.model.update(_.copy(game = Some(newGame)))
        else view.error("Cannot build road")
      case None =>
        ()

  override def onStructureSpot(spot: StructureSpot): Unit =
    println(this.model.state.game.get.state.assignedBuildings)
    this.model.state.game match
      case Some(game) =>
        if game.canPlay(ScatanActions.BuildSettlement(spot, game.turn.player))
        then
          val newGame =
            game
              .play(ScatanActions.BuildSettlement(spot, game.turn.player))
          this.model.update(_.copy(game = Some(newGame)))
        else view.error("Cannot build settlement")
      case None =>
        ()
