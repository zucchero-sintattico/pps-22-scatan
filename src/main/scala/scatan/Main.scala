package scatan
import com.raquo.laminar.api.L.{*, given}
import scatan.example.controller.{HomeController, HomeControllerImpl}
import scatan.example.model.CounterAppState
import scatan.example.view.{HomeView, ScalaJsHomeView}
import scatan.mvc.lib.application.NavigableApplication
import scatan.mvc.lib.page.PageFactory
import scatan.mvc.lib.{Controller, Model, NavigableApplicationManager, ScalaJS}

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

// Application
val CounterApplication: NavigableApplication[CounterAppState, Pages] = NavigableApplication[CounterAppState, Pages](
  initialState = CounterAppState(0),
  pagesFactories = Pages.values.map(p => p -> p.pageFactory).toMap
)

/*
@main def main(): Unit =
  NavigableApplicationManager.startApplication(CounterApplication, Pages.Home)
*/