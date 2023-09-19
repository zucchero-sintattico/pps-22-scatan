package scatan.controllers.home
import scatan.lib.mvc.Controller
import scatan.lib.mvc.BaseController
import scatan.lib.mvc.EmptyController
import scatan.model.ApplicationState
import scatan.views.home.AboutView
import scatan.lib.mvc.NavigableApplicationManager
import scatan.Pages
import scatan.lib.mvc.{NavigableApplicationManager, Controller}

trait AboutController extends Controller

object AboutController:
  def apply(requirements: Controller.Requirements[AboutView, ApplicationState]): AboutController =
    AboutControllerImpl(requirements)

private class AboutControllerImpl(requirements: Controller.Requirements[AboutView, ApplicationState])
    extends BaseController(requirements)
    with AboutController
