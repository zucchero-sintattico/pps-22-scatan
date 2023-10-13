package scatan.controllers.game

import scatan.lib.mvc.{Controller, EmptyController}
import scatan.model.ApplicationState
import scatan.views.game.SetUpView

/** The controller for the game setup screen.
  */
trait SetUpController extends Controller[ApplicationState]:

  /** Starts the game with the given usernames.
    * @param usernames,
    *   the usernames of the players.
    */
  def startGame(usernames: String*): Unit

object SetUpController:
  def apply(requirements: Controller.Requirements[SetUpView, ApplicationState]): SetUpController =
    new EmptyController(requirements) with SetUpController:
      override def startGame(usernames: String*): Unit =
        this.model.update(_.createGame(usernames*))
