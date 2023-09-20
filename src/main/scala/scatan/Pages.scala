package scatan

import scatan.lib.mvc.page.PageFactory
import scatan.model.ApplicationState
import scatan.views.home.HomeView
import scatan.views.home.ScalaJsHomeView
import scatan.controllers.home.HomeController
import scatan.controllers.home.HomeControllerImpl
import scatan.views.game.SetUpView
import scatan.views.game.ScalaJsSetUpView
import scatan.controllers.game.SetUpController
import scatan.controllers.game.SetUpControllerImpl
import scatan.views.home.AboutView
import scatan.views.home.ScalaJSAboutView
import scatan.controllers.home.AboutController
import scatan.controllers.home.AboutControllerImpl
import scatan.views.game.GameView
import scatan.views.game.ScalaJsGameView
import scatan.lib.mvc.EmptyController
import scatan.lib.mvc.Controller
import scatan.lib.mvc.Model
import scatan.lib.mvc.{Controller, View}
import cats.syntax.apply
import scatan.lib.mvc.ScalaJSView
import scatan.lib.mvc.page.ScalaJSPageFactory
import scatan.controllers.game.GameController

given root: String = "root"

enum Pages(val pageFactory: PageFactory[?, ?, ApplicationState]):
  case Home extends Pages(ScalaJSPageFactory(HomeView.apply, HomeController.apply))
  case SetUp extends Pages(ScalaJSPageFactory(SetUpView.apply, SetUpController.apply))
  case About extends Pages(ScalaJSPageFactory(AboutView.apply, AboutController.apply))
  case Game extends Pages(ScalaJSPageFactory(GameView.apply, GameController.apply))
