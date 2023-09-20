package scatan.views.game
import scatan.Pages
import scatan.controllers.game.SetUpController
import com.raquo.laminar.api.L.*
import scatan.Pages
import scatan.lib.mvc.{View, BaseScalaJSView}
import org.scalajs.dom.document

/** This is the view for the setup page.
  */
trait SetUpView extends View:
  /** This method is called when the user clicks the start button.
    */
  def switchToGame(): Unit

  /** This method is called when the user clicks the back button.
    */
  def switchToHome(): Unit

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
  override def switchToGame(): Unit =
    val usernames =
      for i <- 1 to numberOfUsers
      yield document
        .getElementsByClassName("setup-menu-textbox")
        .item(i - 1)
        .asInstanceOf[org.scalajs.dom.raw.HTMLInputElement]
        .value
    this.navigateTo(Pages.Game)

  override def switchToHome(): Unit =
    this.navigateTo(Pages.Home)

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
          onClick --> (_ => this.switchToHome()),
          "Back"
        ),
        button(
          cls := "setup-menu-button",
          onClick --> (_ => this.switchToGame()),
          "Start"
        )
      )
    )
