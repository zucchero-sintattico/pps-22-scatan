package scatan
import com.raquo.laminar.api.L.{*, given}
import scatan.mvc.lib.application.NavigableApplication
import scatan.mvc.lib.page.PageFactory
import scatan.mvc.lib.{Controller, Model, NavigableApplicationManager, ScalajsView, View}

import scala.util.Random

// Model
case class CounterAppState(counter: Int) extends Model.State

// View
class HomeView(requirements: View.Requirements[HomeController], container: String)
    extends ScalajsView[HomeController](requirements, container):

  private val reactiveCounter = Var(initial = 0)

  def onCounterUpdate(): Unit =
    println("Counter updated: " + controller.counter)
    reactiveCounter.set(controller.counter)
    println("Reactive counter updated: " + reactiveCounter.now())

  override def element: Element =
    div(
      h1("Hello, world!"),
      p(
        child.text <-- reactiveCounter.signal.map(_.toString)
      ),
      button(
        "Increment",
        onClick --> (_ => controller.increment())
      )
    )

// Controller
class HomeController(requirements: Controller.Requirements[HomeView, CounterAppState])
    extends Controller[HomeView, CounterAppState](requirements):

  def counter: Int = model.state.counter

  def increment(): Unit =
    println("Incrementing counter in controller")
    model.state = model.state.copy(counter = model.state.counter + 1)
    view.onCounterUpdate()

// Route
enum Pages(val pageFactory: PageFactory[?, ?, CounterAppState]):
  case Home
      extends Pages(
        PageFactory(
          viewFactory = new HomeView(_, "root"),
          controllerFactory = new HomeController(_)
        )
      )

// Application
val CounterApplication: NavigableApplication[CounterAppState, Pages] = NavigableApplication[CounterAppState, Pages](
  initialState = CounterAppState(0),
  pagesFactories = Pages.values.map(p => p -> p.pageFactory).toMap
)

@main def main(): Unit =
  NavigableApplicationManager.startApplication(CounterApplication, Pages.Home)
