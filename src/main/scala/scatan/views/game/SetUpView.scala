package scatan.views.game

import scatan.controllers.game.SetUpController

import scatan.Pages
import scatan.lib.mvc.View
import scatan.controllers.game.SetUpController
import com.raquo.laminar.api.L.*
import scatan.lib.mvc.{ScalaJSView, View}
import scatan.Pages
import scatan.lib.mvc.{View, BaseScalaJSView}
import org.scalajs.dom.document

/** This is the view for the setup page.
  */
trait SetUpView extends View:

  /** Notify the controller to switch to the game page.
    */
  def notifySwitchToGame(): Unit

  /** Notify the controller to switch to the home page.
    */
  def notifySwitchToHome(): Unit

object SetUpView:
  def apply(container: String, requirements: View.Requirements[SetUpController]): SetUpView =
    ScalaJsSetUpView(container, requirements)

/** This is the view for the setup page.
  *
  * @param requirements,
  *   the requirements for the view
  * @param container,
  *   the container for the view
  */
private class ScalaJsSetUpView(container: String, requirements: View.Requirements[SetUpController])
    extends BaseScalaJSView(container, requirements)
    with SetUpView:

  val numberOfUsers: Int = 3

  @SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
  override def notifySwitchToGame(): Unit =
    val usernames =
      for i <- 1 to numberOfUsers
      yield document
        .getElementsByClassName("setup-menu-textbox")
        .item(i - 1)
        .asInstanceOf[org.scalajs.dom.raw.HTMLInputElement]
        .value
    // controller.goToPlay(usernames*)

  override def notifySwitchToHome(): Unit = ???
  // controller.goToHome()

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
          onClick --> (_ => this.navigateTo(Pages.Home)),
          "Back"
        ),
        button(
          cls := "setup-menu-button",
          onClick --> (_ => this.navigateTo(Pages.Game)),
          "Start"
        )
      )
    )
