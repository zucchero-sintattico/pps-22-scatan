package scatan.controllers.game

import scatan.lib.mvc.{BaseController, Controller}
import scatan.model.ApplicationState
import scatan.views.game.SetUpView
import scatan.model.GameMap

/** This is the controller for the setup page.
  */
trait SetUpController extends Controller[ApplicationState]:
  def startGame(gameMap: GameMap, usernames: String*): Unit

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

  override def startGame(gameMap: GameMap, usernames: String*): Unit =
    this.model.update(_.createGame(gameMap, usernames*))
