package scatan.controllers.setup

import scatan.mvc.lib.Controller
import scatan.views.setup.SetUpView
import scatan.mvc.lib.NavigableApplicationManager
import scatan.Pages

/** This is the controller for the setup page.
  */
trait SetUpController extends Controller:
  /** This method is called when the user clicks on the back button.
    */
  def goToHome(): Unit

  /** This method is called when the user clicks on the start button.
    */
  def goToPlay(): Unit

  /** This is the implementation of the controller for the setup page.
    * @param dependencies,
    *   the dependencies for the controller.
    */
class SetUpControllerImpl(dependencies: Controller.Requirements[SetUpView, ?]) extends SetUpController:
  override def goToHome(): Unit = NavigableApplicationManager.navigateTo(Pages.Home)
  // TODO: implement goToPlay
  override def goToPlay(): Unit = NavigableApplicationManager.navigateTo(Pages.Home)
