package scatan
import com.raquo.laminar.api.L.given
import scatan.lib.mvc.NavigableApplicationManager
import scatan.lib.mvc.application.NavigableApplication
import scatan.model.ApplicationState
import scatan.lib.mvc.page.PageFactory
import scatan.lib.mvc.ScalaJSView
import scatan.lib.mvc.{Controller, Model, NavigableApplicationManager}

import scala.util.Random
import lib.mvc.application.NavigableApplication
import lib.mvc.page.PageFactory
import lib.mvc.{NavigableApplicationManager, Model, Controller}
import scatan.lib.mvc.EmptyController

val Application = NavigableApplication(
  initialState = ApplicationState(),
  pagesFactories = Pages.values.map(p => p -> p.pageFactory).toMap
)

@main def main(): Unit =
  NavigableApplicationManager.startApplication(Application, Pages.Home)
