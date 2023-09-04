package scatan.mvc

import scatan.mvc.lib.{page, *}
import scatan.mvc.lib.application.NavigableApplication
import scatan.mvc.lib.page.PageFactory

case class MyState(value: Int) extends Model.State

class HomeView(requirements: View.Requirements[HomeController]) extends View[HomeController](requirements):
  override def show(): Unit =
    println("HomeView.show")
    renderHome()

  override def hide(): Unit =
    println("HomeView.hide")

  def renderHome(): Unit =
    println("HomeView.renderHome")
    controller.controlHome()

class HomeController(requirements: Controller.Requirements[HomeView, MyState])
    extends Controller[HomeView, MyState](requirements):
  def controlHome(): Unit =
    println("HomeController.controlHome")

class AboutView(requirements: View.Requirements[AboutController]) extends View[AboutController](requirements):
  override def show(): Unit =
    println("AboutView.show")
    renderAbout()

  override def hide(): Unit =
    println("AboutView.hide")

  def renderAbout(): Unit =
    println("AboutView.renderAbout")
    controller.controlAbout()

class AboutController(requirements: Controller.Requirements[AboutView, MyState])
    extends Controller[AboutView, MyState](requirements):
  def controlAbout(): Unit =
    println("AboutController.controlAbout")

enum Pages(val factory: PageFactory[?, ?, MyState]):
  private def toMapEntry: (Pages, PageFactory[?, ?, MyState]) = this -> factory
  case Home
      extends Pages(
        PageFactory(
          viewFactory = new HomeView(_),
          controllerFactory = new HomeController(_)
        )
      )
  case About
      extends Pages(
        PageFactory(
          viewFactory = new AboutView(_),
          controllerFactory = new AboutController(_)
        )
      )

object Pages:
  // Implicit conversion from Pages enum to Map[Pages, PageFactory[?, ?]]
  given Conversion[Pages.type, Map[Pages, PageFactory[?, ?, MyState]]] = _.values.map(_.toMapEntry).toMap

val MyApplication = NavigableApplication[MyState, Pages](
  initialState = MyState(0),
  pagesFactories = Pages
)

@main def run(): Unit =
  val app = MyApplication
  app.show(Pages.Home)
  app.show(Pages.About)
  app.back()
