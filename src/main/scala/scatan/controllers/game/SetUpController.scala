package scatan.controllers.game

import scatan.lib.mvc.Controller
import scatan.views.game.SetUpView
import scatan.lib.mvc.NavigableApplicationManager
import scatan.Pages
import scatan.model.ApplicationState

/** This is the controller for the setup page.
  */
trait SetUpController extends Controller:
  /** This method is called when the user clicks on the back button.
    */
  def goToHome(): Unit

  /** This method is called when the user clicks on the start button.
    */
  def goToPlay(usernames: String*): Unit

  def createGame(usernames: String*): Unit

  /** This is the implementation of the controller for the setup page.
    * @param requirements,
    *   the requirements for the controller.
    */
class SetUpControllerImpl(requirements: Controller.Requirements[SetUpView, ApplicationState])
    extends SetUpController
    with Controller.Dependencies(requirements):

  private def checkIfAllNamesAreInserted(usernames: Seq[String]): Boolean =
    usernames.forall(_.matches(".*\\S.*"))

  override def goToHome(): Unit =
    NavigableApplicationManager.navigateTo(Pages.Home)
  override def goToPlay(usernames: String*): Unit =
    if checkIfAllNamesAreInserted(usernames) then NavigableApplicationManager.navigateTo(Pages.Game)

  override def createGame(usernames: String*): Unit =
    this.model.state.createGame(usernames*)
