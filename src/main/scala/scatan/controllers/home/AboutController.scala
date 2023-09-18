package scatan.controllers.home
import scatan.mvc.lib.Controller
import scatan.model.ApplicationState
import scatan.views.home.AboutView
import scatan.mvc.lib.NavigableApplicationManager
import scatan.Pages

trait AboutController extends Controller:
  def goToHome(): Unit

class AboutControllerImpl(requirements: Controller.Requirements[AboutView, ApplicationState])
    extends AboutController
    with Controller.Dependencies(requirements):
  override def goToHome(): Unit =
    NavigableApplicationManager.navigateTo(Pages.Home)
