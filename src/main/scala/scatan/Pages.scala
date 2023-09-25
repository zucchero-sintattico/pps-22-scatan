package scatan

import scatan.controllers.game.{GameController, SetUpController}
import scatan.controllers.home.{AboutController, HomeController}
import scatan.lib.mvc.page.{PageFactory, ScalaJSPageFactory}
import scatan.model.ApplicationState
import scatan.views.game.{GameView, SetUpView}
import scatan.views.home.{AboutView, HomeView}

given root: String = "root"

enum Pages(val pageFactory: PageFactory[?, ?, ApplicationState]):
  case Home extends Pages(ScalaJSPageFactory(HomeView.apply, HomeController.apply))
  case SetUp extends Pages(ScalaJSPageFactory(SetUpView.apply, SetUpController.apply))
  case About extends Pages(ScalaJSPageFactory(AboutView.apply, AboutController.apply))
  case Game extends Pages(ScalaJSPageFactory(GameView.apply, GameController.apply))
