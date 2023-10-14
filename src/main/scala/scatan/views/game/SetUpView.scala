package scatan.views.game
import com.raquo.laminar.api.L.*
import org.scalajs.dom.document
import scatan.Pages
import scatan.controllers.game.SetUpController
import scatan.lib.mvc.{BaseScalaJSView, View}
import scatan.model.ApplicationState
import scatan.model.map.{GameMap, GameMapFactory}
import scatan.views.game.MapSelectionMode.*
import scatan.views.game.components.LeftTabComponent.buttonsComponent
import scatan.views.game.components.MapComponent

enum MapSelectionMode:
  case Default, Random, WithIterator

/** This is the view for the setup page.
  */
trait SetUpView extends View[ApplicationState]

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
    extends BaseScalaJSView[ApplicationState, SetUpController](container, requirements)
    with SetUpView:

  private val numberOfUsers: Var[Int] = Var(3)
  private val reactiveNumberOfUsers: Signal[Int] = numberOfUsers.signal

  val mapSelectionMode: Var[MapSelectionMode] = Var(MapSelectionMode.Default)
  val reactiveGameMap: Var[GameMap] = Var(GameMapFactory.defaultMap)

  private def changeMap: Unit =
    mapSelectionMode.now() match
      case Default =>
        reactiveGameMap.set(GameMapFactory.defaultMap)
      case Random =>
        reactiveGameMap.set(GameMapFactory.randomMap)
      case WithIterator =>
        reactiveGameMap.set(GameMapFactory.nextPermutation)

  private def validateNames(usernames: String*) =
    usernames.forall(_.matches(".*\\S.*"))

  private def switchToGame(): Unit =
    val usernames =
      for i <- 1 to numberOfUsers.now()
      yield document
        .getElementsByClassName("setup-menu-textbox")
        .item(i - 1)
        .asInstanceOf[org.scalajs.dom.raw.HTMLInputElement]
        .value
    if validateNames(usernames*) then
      println(usernames)
      this.controller.startGame(reactiveGameMap.now(), usernames*)
      this.navigateTo(Pages.Game)

  private def switchToHome(): Unit =
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
              defaultValue := s"Player $i",
              placeholder := s"Player $i"
            )
          )
        )
      ),
      button(
        cls := "setup-menu-button",
        onClick --> (_ => this.switchToGame()),
        "Start"
      ),
      button(
        cls := "setup-menu-button",
        onClick --> (_ => this.switchToHome()),
        "Back"
      ),
      div(
        cls := "setup-menu-map-picker",
        div(
          cls := "setup-menu-map-picker-left-tab",
          select(
            cls := "setup-menu-map-picker-combobox",
            onChange.mapToValue.map(v => MapSelectionMode.fromOrdinal(v.toInt)) --> mapSelectionMode,
            for (mode <- MapSelectionMode.values)
              yield option(
                value := s"${mode.ordinal}",
                mode.toString
              )
          ),
          button(
            cls := "setup-menu-map-picker-button",
            "Change Map",
            onClick --> (_ => changeMap)
          )
        ),
        div(
          cls := "setup-menu-map",
          child <-- reactiveGameMap.signal.map(gameMap => MapComponent.map(using gameMap))
        )
      )
    )
