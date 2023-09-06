package scatan.controllers.home

import scatan.mvc.lib.Controller
import scatan.views.home.HomeView
import scatan.Pages
import scatan.mvc.lib.application.NavigableApplication
import scatan.mvc.lib.NavigableApplicationManager

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
class HomeControllerImpl(requirements: Controller.Requirements[HomeView, ?]) extends HomeController:
  override def goToSetup(): Unit =
    NavigableApplicationManager.navigateTo[Pages](Pages.Setup)

  override def goToAbout(): Unit =
    NavigableApplicationManager.navigateTo[Pages](Pages.About)
