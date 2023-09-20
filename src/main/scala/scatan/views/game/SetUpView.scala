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

  val numberOfUsers: Var[Int] = Var(3)
  val reactiveNumberOfUsers: Signal[Int] = numberOfUsers.signal

  private def validateNames(usernames: String*) =
    usernames.forall(_.matches(".*\\S.*"))

  @SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
  override def switchToGame(): Unit =
    val usernames =
      for i <- 1 to numberOfUsers.now()
      yield document
        .getElementsByClassName("setup-menu-textbox")
        .item(i - 1)
        .asInstanceOf[org.scalajs.dom.raw.HTMLInputElement]
        .value
    if validateNames(usernames*) then
      println(usernames)
      // this.controller.startGame(usernames*)
      print(this.controller)
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
        // combobox for choose 3 or 4 players
        select(
          cls := "setup-menu-combobox",
          onChange.mapToValue.map(_.toInt) --> numberOfUsers,
          option(
            cls := "setup-menu-combobox-option",
            value := "3",
            "3 Players"
            // on select, change the number of users
          ),
          option(
            cls := "setup-menu-combobox-option",
            value := "4",
            "4 Players"
          )
        )
      ),
      div(
        cls := "setup-menu",
        children <-- reactiveNumberOfUsers.map(element =>
          for i <- 1 to element
          yield div(
            cls := "setup-menu-textbox-container",
            input(
              cls := "setup-menu-textbox",
              placeholder := s"Player $i"
            )
          )
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
