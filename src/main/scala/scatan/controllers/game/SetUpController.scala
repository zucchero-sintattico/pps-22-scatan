package scatan.controllers.game

import scatan.lib.mvc.{Controller, EmptyController}
import scatan.model.ApplicationState
import scatan.model.map.GameMap
import scatan.views.game.SetUpView

/** The controller for the game setup screen.
  */
trait SetUpController extends Controller[ApplicationState]:

  /** Starts the game with the given usernames.
    * @param gameMap,
    *   the game map to use.
    * @param usernames,
    *   the usernames of the players.
    */
  def startGame(gameMap: GameMap, usernames: String*): Unit

object SetUpController:
  def apply(requirements: Controller.Requirements[SetUpView, ApplicationState]): SetUpController =
    new EmptyController(requirements) with SetUpController:
      override def startGame(gameMap: GameMap, usernames: String*): Unit =
        this.model.update(_.createGame(gameMap, usernames*))
