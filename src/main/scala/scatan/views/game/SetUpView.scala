package scatan.views.game

import scatan.controllers.game.SetUpController

import scatan.Pages
import scatan.mvc.lib.View
import scatan.controllers.game.SetUpController
import com.raquo.laminar.api.L.*
import scatan.mvc.lib.{ScalaJSView, View}
import scatan.Pages
import org.scalajs.dom.document

/** This is the view for the setup page.
  */
trait SetUpView extends View:
  def notifySwitchToGame(): Unit

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

  override def notifySwitchToGame(): Unit =
    // obtain the usernames from the textboxes and pass them to the controller
    val usernames =
      for i <- 1 to numberOfUsers
      yield document
        .getElementsByClassName("setup-menu-textbox")
        .item(i - 1)
        .asInstanceOf[org.scalajs.dom.raw.HTMLInputElement]
        .value
    controller.goToHome(usernames*)

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
          onClick --> (_ => this.notifySwitchToGame()),
          "Start"
        )
      )
    )
