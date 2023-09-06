package scatan
import com.raquo.laminar.api.L.{*, given}
import scatan.example.controller.{AboutController, AboutControllerImpl}
import scatan.controllers.home.{HomeController, HomeControllerImpl}
import scatan.views.home.{HomeView, ScalaJsHomeView}
import scatan.example.model.CounterAppState
import scatan.example.view.{AboutView, ScalaJSAboutView}
import scatan.mvc.lib.application.NavigableApplication
import scatan.mvc.lib.page.PageFactory
import scatan.mvc.lib.{Controller, Model, NavigableApplicationManager, ScalaJSView}

import scala.util.Random

// Route
enum Pages(val pageFactory: PageFactory[?, ?, CounterAppState]):
  case Home
      extends Pages(
        PageFactory[HomeController, HomeView, CounterAppState](
          viewFactory = new ScalaJsHomeView(_, "root"),
          controllerFactory = new HomeControllerImpl(_)
        )
      )
  case About
      extends Pages(
        PageFactory[AboutController, AboutView, CounterAppState](
          viewFactory = new ScalaJSAboutView(_, "root"),
          controllerFactory = new AboutControllerImpl(_)
        )
      )

// Application
val CounterApplication: NavigableApplication[CounterAppState, Pages] = NavigableApplication[CounterAppState, Pages](
  initialState = CounterAppState(0),
  pagesFactories = Pages.values.map(p => p -> p.pageFactory).toMap
)

@main def main(): Unit =
  NavigableApplicationManager.startApplication(CounterApplication, Pages.Home)
