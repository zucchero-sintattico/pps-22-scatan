package scatan.controllers.game

import scatan.mvc.lib.Controller.Requirements
import scatan.mvc.lib.NavigableApplicationManager
import scatan.Pages
import scatan.model.ApplicationState
import scatan.mvc.lib.Controller
import scatan.views.game.GameView

trait GameController extends Controller:
  def goToHome(): Unit

class GameControllerImpl(requirements: Controller.Requirements[GameView, ApplicationState])
    extends GameController
    with Controller.Dependencies(requirements):
  def goToHome(): Unit =
    NavigableApplicationManager.navigateTo(Pages.Home)
