package scatan.controllers.game

import scatan.lib.mvc.Controller.Requirements
import scatan.lib.mvc.NavigableApplicationManager
import scatan.Pages
import scatan.model.ApplicationState
import scatan.lib.mvc.Controller
import scatan.views.game.GameView
import scatan.lib.mvc.BaseController

trait GameController extends Controller

object GameController:
  def apply(requirements: Controller.Requirements[GameView, ApplicationState]): GameController =
    GameControllerImpl(requirements)

private class GameControllerImpl(requirements: Controller.Requirements[GameView, ApplicationState])
    extends BaseController(requirements)
    with GameController
