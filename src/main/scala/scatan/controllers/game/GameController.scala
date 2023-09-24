package scatan.controllers.game

import scatan.Pages
import scatan.model.ApplicationState
import scatan.views.game.GameView
import scatan.Pages
import scatan.lib.mvc.{BaseController, Controller}
import scatan.model.ApplicationState

trait GameController extends Controller

object GameController:
  def apply(requirements: Controller.Requirements[GameView, ApplicationState]): GameController =
    GameControllerImpl(requirements)

private class GameControllerImpl(requirements: Controller.Requirements[GameView, ApplicationState])
    extends BaseController(requirements)
    with GameController
