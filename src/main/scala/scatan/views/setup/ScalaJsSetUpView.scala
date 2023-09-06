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
  val numberOfUsers: Int = 3

  def start(): Unit =
    print("Hello, world!")

  override def element: Element =
    /*
      in this div, there are 4 textbox, one for each user name, and one button to start the game
     */
    div(
      cls := "setup-view",
      // Title
      div(
        cls := "setup-title",
        "Setup your game"
      ),
      // Menu view with 3 buttons, play, settings and about, dispose them vertically
      div(
        cls := "setup-menu",
        for i <- 1 to numberOfUsers
        yield div(
          cls := "setup-menu-textbox-container",
          input(
            cls := "setup-menu-textbox",
            placeholder := "Player " + i
          ),
          label(
            cls := "setup-menu-label",
            "Player" + i
          )
        ),
        button(
          cls := "setup-menu-button",
          onClick --> (_ => controller.goToHome()),
          "Back"
        ),
        button(
          cls := "setup-menu-button",
          onClick --> (_ => controller.goToPlay()),
          "Start"
        )
      )
    )
