package scatan.controllers.home

import scatan.lib.mvc.{BaseController, Controller}
import scatan.model.ApplicationState
import scatan.views.home.HomeView

/** This is the controller for the home page.
  */
trait HomeController extends Controller[ApplicationState]

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
