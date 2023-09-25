package scatan.controllers.home

import scatan.views.home.HomeView
import scatan.Pages
import scatan.model.ApplicationState
import scatan.lib.mvc.{BaseController, Controller}
import scatan.model.ApplicationState

/** This is the controller for the home page.
  */
trait HomeController extends Controller

object HomeController:
  def apply(requirements: Controller.Requirements[HomeView, ApplicationState]): HomeController =
    HomeControllerImpl(requirements)

/** This is the implementation of the controller for the home page.
  * @param requirements,
  *   the requirements for the controller.
  */
class HomeControllerImpl(requirements: Controller.Requirements[HomeView, ApplicationState])
    extends BaseController(requirements)
    with HomeController
