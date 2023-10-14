package scatan.controllers.home

import scatan.lib.mvc.{Controller, EmptyController}
import scatan.model.ApplicationState
import scatan.views.home.AboutView

/** The about page controller.
  */
trait AboutController extends Controller[ApplicationState]

object AboutController:
  def apply(requirements: Controller.Requirements[AboutView, ApplicationState]): AboutController =
    new EmptyController(requirements) with AboutController
