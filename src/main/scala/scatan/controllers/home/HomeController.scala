package scatan.controllers.home

import scatan.lib.mvc.{Controller, EmptyController}
import scatan.model.ApplicationState
import scatan.views.home.HomeView

/** The home page controller.
  */
trait HomeController extends Controller[ApplicationState]

object HomeController:
  def apply(requirements: Controller.Requirements[HomeView, ApplicationState]): HomeController =
    new EmptyController(requirements) with HomeController
