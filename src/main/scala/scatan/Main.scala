package scatan
import com.raquo.laminar.api.L.{*, given}
import scatan.controllers.home.{HomeController, HomeControllerImpl}
import scatan.controllers.game.{SetUpController, SetUpControllerImpl}
import scatan.views.game.{SetUpView, ScalaJsSetUpView}
import scatan.views.home.{HomeView, ScalaJsHomeView}
import scatan.model.ApplicationState
import scatan.views.home.{AboutView, ScalaJSAboutView}
import scatan.controllers.home.{AboutController, AboutControllerImpl}
import scatan.mvc.lib.application.NavigableApplication
import scatan.mvc.lib.page.PageFactory
import scatan.mvc.lib.{Controller, Model, NavigableApplicationManager, ScalaJSView}

import scala.util.Random

// Route
enum Pages(val pageFactory: PageFactory[?, ?, ApplicationState]):
  case Home
      extends Pages(
        PageFactory[HomeController, HomeView, ApplicationState](
          viewFactory = new ScalaJsHomeView(_, "root"),
          controllerFactory = new HomeControllerImpl(_)
        )
      )
  case Setup
      extends Pages(
        PageFactory[SetUpController, SetUpView, ApplicationState](
          viewFactory = new ScalaJsSetUpView(_, "root"),
          controllerFactory = new SetUpControllerImpl(_)
        )
      )
  case About
      extends Pages(
        PageFactory[AboutController, AboutView, ApplicationState](
          viewFactory = new ScalaJSAboutView(_, "root"),
          controllerFactory = new AboutControllerImpl(_)
        )
      )

// App
val Application: NavigableApplication[ApplicationState, Pages] = NavigableApplication[ApplicationState, Pages](
  initialState = ApplicationState(),
  pagesFactories = Pages.values.map(p => p -> p.pageFactory).toMap
)

@main def main(): Unit =
  NavigableApplicationManager.startApplication(Application, Pages.Home)
