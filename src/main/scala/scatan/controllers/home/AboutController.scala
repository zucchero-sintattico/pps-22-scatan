package scatan.controllers.home
import scatan.mvc.lib.Controller
import scatan.model.ApplicationState
import scatan.views.home.AboutView

trait AboutController extends Controller:
  def about(): Unit

class AboutControllerImpl(requirements: Controller.Requirements[AboutView, ApplicationState])
    extends AboutController
    with Controller.Dependencies(requirements):
  override def about(): Unit =
    println("About")
