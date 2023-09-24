package scatan.controllers.game

import scatan.lib.mvc.BaseController
import scatan.views.game.SetUpView
import scatan.lib.mvc.NavigableApplicationManager
import scatan.Pages
import scatan.model.ApplicationState
import scatan.lib.mvc.Controller

/** This is the controller for the setup page.
  */
trait SetUpController extends Controller:
  def startGame(usernames: String*): Unit

object SetUpController:
  def apply(requirements: Controller.Requirements[SetUpView, ApplicationState]): SetUpController =
    SetUpControllerImpl(requirements)

/** This is the implementation of the controller for the setup page.
  * @param requirements,
  *   the requirements for the controller.
  */
private class SetUpControllerImpl(requirements: Controller.Requirements[SetUpView, ApplicationState])
    extends BaseController(requirements)
    with SetUpController:

  override def startGame(usernames: String*): Unit =
    this.model.update(_.createGame(usernames*))
    println(this.model.state)
