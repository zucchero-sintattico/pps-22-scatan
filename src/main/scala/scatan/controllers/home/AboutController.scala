package scatan.controllers.home

import scatan.Pages
import scatan.lib.mvc.{BaseController, Controller}
import scatan.model.ApplicationState
import scatan.views.home.AboutView

trait AboutController extends Controller

object AboutController:
  def apply(requirements: Controller.Requirements[AboutView, ApplicationState]): AboutController =
    AboutControllerImpl(requirements)

private class AboutControllerImpl(requirements: Controller.Requirements[AboutView, ApplicationState])
    extends BaseController(requirements)
    with AboutController
