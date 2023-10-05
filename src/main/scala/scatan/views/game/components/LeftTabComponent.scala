package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.controllers.game.{GameController, InitialAssignmentController}
import scatan.lib.mvc.ScalaJSView
import scatan.model.ApplicationState
import scatan.model.game.config.ScatanActions
import scatan.views.game.{GameView, InitialAssignmentView}

object LeftTabComponent:

  extension (action: ScatanActions) def toViewAction: String = action.toString

  def leftTabCssClass: String = "game-view-left-tab"

  def currentPlayerComponent(using view: Signal[ApplicationState]): Element =
    h1(
      className := "game-view-player",
      child.text <-- view
        .map("Current Player: " + _.game.map(_.turn.player.name).getOrElse("No player"))
    )

  def possibleMovesComponent(using view: Signal[ApplicationState]): Element =
    div(
      className := "game-view-moves",
      "Possible moves:",
      ul(
        children <-- view
          .map(state =>
            for move <- state.game.map(_.allowedActions.toSeq).getOrElse(Seq.empty)
            yield li(cls := "game-view-move", move.toViewAction)
          )
      )
    )

  def buttonsComponent(using view: Signal[ApplicationState])(using controller: GameController): Element =
    div(
      className := "game-view-buttons",
      button(
        className := "game-view-button roll-dice-button",
        "Roll dice",
        onClick --> { _ => controller.rollDice() }
      ),
      button(
        className := "game-view-button end-turn-button",
        "End Turn",
        onClick --> { _ => controller.nextTurn() }
      )
    )
