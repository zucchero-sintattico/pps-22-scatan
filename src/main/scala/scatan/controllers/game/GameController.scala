package scatan.controllers.game

import scatan.Pages
import scatan.model.ApplicationState
import scatan.views.game.GameView
import scatan.Pages
import scatan.lib.game.Game
import scatan.lib.mvc.{BaseController, Controller}
import scatan.model.ApplicationState

trait GameController extends Controller[ApplicationState]:
  def nextTurn(): Unit

object GameController:
  def apply(requirements: Controller.Requirements[GameView, ApplicationState]): GameController =
    GameControllerImpl(requirements)

private class GameControllerImpl(requirements: Controller.Requirements[GameView, ApplicationState])
    extends BaseController(requirements)
    with GameController:

  override def nextTurn(): Unit =
    this.model.state.game
      .foreach(_.nextTurn.foreach(nextTurnGame => this.model.update(_.copy(game = Some(nextTurnGame)))))
