package scatan.controllers.home
import scatan.lib.mvc.Controller
import scatan.model.ApplicationState
import scatan.views.home.AboutView
import scatan.lib.mvc.NavigableApplicationManager
import scatan.Pages
import scatan.lib.mvc.{NavigableApplicationManager, Controller}

trait AboutController extends Controller:
  def goToHome(): Unit

class AboutControllerImpl(requirements: Controller.Requirements[AboutView, ApplicationState])
    extends AboutController
    with Controller.Dependencies(requirements):
  override def goToHome(): Unit =
    NavigableApplicationManager.navigateTo(Pages.Home)
