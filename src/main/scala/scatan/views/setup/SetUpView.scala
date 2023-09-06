package scatan.views.setup

import scatan.controllers.setup.SetUpController

import scatan.Pages
import scatan.mvc.lib.View
import scatan.controllers.setup.SetUpController
import com.raquo.laminar.api.L.*
import scatan.mvc.lib.{NavigableApplicationManager, ScalaJSView, View}
import scatan.Pages

/** This is the view for the setup page.
  */
trait SetUpView extends View:
  def start(): Unit

/** This is the view for the setup page.
  *
  * @param requirements,
  *   the requirements for the view
  * @param container,
  *   the container for the view
  */
class ScalaJsSetUpView(requirements: View.Requirements[SetUpController], container: String)
    extends SetUpView
    with View.Dependencies(requirements)
    with ScalaJSView(container):
  val numberOfUsers: Int = 3

  def start(): Unit =
    print("Hello, world!")

  override def element: Element =
    div(
      cls := "setup-view",
      // Title
      div(
        cls := "setup-title",
        "Setup your game"
      ),
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
