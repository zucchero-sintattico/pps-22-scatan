package scatan.controllers.home

import scatan.lib.mvc.Controller
import scatan.views.home.HomeView
import scatan.Pages
import scatan.lib.mvc.application.NavigableApplication
import scatan.lib.mvc.NavigableApplicationManager
import scatan.model.ApplicationState
import scatan.lib.mvc.application.NavigableApplication
import scatan.lib.mvc.{NavigableApplicationManager, Controller}

/** This is the controller for the home page.
  */
trait HomeController extends Controller:
  /** This method is called when the user clicks on the settings button.
    */
  def goToSetup(): Unit

  /** This method is called when the user clicks on the about button.
    */
  def goToAbout(): Unit

/** This is the implementation of the controller for the home page.
  * @param requirements,
  *   the requirements for the controller.
  */
class HomeControllerImpl(requirements: Controller.Requirements[HomeView, ApplicationState]) extends HomeController:
  override def goToSetup(): Unit =
    NavigableApplicationManager.navigateTo[Pages](Pages.Setup)

  override def goToAbout(): Unit =
    NavigableApplicationManager.navigateTo[Pages](Pages.About)
