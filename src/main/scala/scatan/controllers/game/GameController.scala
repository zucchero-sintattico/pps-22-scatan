package scatan.controllers.game

import scatan.lib.mvc.Controller.Requirements
import scatan.lib.mvc.NavigableApplicationManager
import scatan.Pages
import scatan.model.ApplicationState
import scatan.lib.mvc.Controller
import scatan.views.game.GameView

trait GameController extends Controller:
  def goToHome(): Unit

class GameControllerImpl(requirements: Controller.Requirements[GameView, ApplicationState])
    extends GameController
    with Controller.Dependencies(requirements):
  def goToHome(): Unit =
    NavigableApplicationManager.navigateTo(Pages.Home)

  def apply(): Unit =
    println("GameController")
