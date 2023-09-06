package scatan.views.setup

import scatan.mvc.lib.View
import scatan.controllers.setup.SetUpController
import com.raquo.laminar.api.L.*
import scatan.mvc.lib.{NavigableApplicationManager, ScalaJSView, View}
import scatan.Pages

class ScalaJsSetUpView(requirements: View.Requirements[SetUpController], container: String)
    extends SetUpView
    with View.Dependencies(requirements)
    with ScalaJSView(container):

  def start(): Unit =
    print("Hello, world!")

  override def element: Element =
    // menu with 4 textbox for the players username and 4 dropdown for the players color and a button to start
    div(
      cls := "setup-view",
      // Title
      div(
        cls := "setup-title"
      ),
      // Menu view with 3 buttons, play, settings and about, dispose them vertically
      div(
        cls := "setup-menu",
        button(
          cls := "setup-menu-button",
          onClick.mapTo(Pages.Home) --> NavigableApplicationManager.navigateTo,
          "Play"
        ),
        button(
          cls := "setup-menu-button",
          onClick.mapTo(Pages.About) --> NavigableApplicationManager.navigateTo,
          "About"
        )
      )
    )
