package scatan.controllers.game

import scatan.mvc.lib.Controller
import scatan.views.game.SetUpView
import scatan.mvc.lib.NavigableApplicationManager
import scatan.Pages
import scatan.model.ApplicationState

/** This is the controller for the setup page.
  */
trait SetUpController extends Controller:
  /** This method is called when the user clicks on the back button.
    */
  def goToHome(usernames: String*): Unit

  /** This method is called when the user clicks on the start button.
    */
  def goToPlay(): Unit

  /** This is the implementation of the controller for the setup page.
    * @param requirements,
    *   the requirements for the controller.
    */
class SetUpControllerImpl(requirements: Controller.Requirements[SetUpView, ApplicationState])
    extends SetUpController
    with Controller.Dependencies(requirements):

  private def checkIfAllNamesAreInserted(usernames: Seq[String]): Boolean =
    // check if the usernames are all non-empty and valid (not only spaces) with regex
    usernames.forall(_.matches(".*\\S.*"))

  override def goToHome(usernames: String*): Unit =
    if checkIfAllNamesAreInserted(usernames) then NavigableApplicationManager.navigateTo(Pages.Game)
  // TODO: implement goToPlay
  override def goToPlay(): Unit = NavigableApplicationManager.navigateTo(Pages.Game)
