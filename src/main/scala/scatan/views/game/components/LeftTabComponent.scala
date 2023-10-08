package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.controllers.game.GameController
import scatan.lib.mvc.ScalaJSView
import scatan.model.ApplicationState
import scatan.model.game.config.ScatanActions
import scatan.views.game.GameView

object LeftTabComponent:

  extension (action: ScatanActions) def toViewAction: String = action.toString

  def leftTabCssClass: String = "game-view-left-tab"

  def currentPlayerComponent(using view: Signal[ApplicationState]): Element =
    div(
      h2(
        className := "game-view-player",
        child.text <-- view
          .map("Current Player: " + _.game.map(_.turn.player.name).getOrElse("No player"))
      ),
      h2(
        className := "game-view-phase",
        child.text <-- view
          .map("Phase: " + _.game.map(_.gameStatus.phase.toString).getOrElse("No phase"))
      ),
      h2(
        className := "game-view-step",
        child.text <-- view
          .map("Step: " + _.game.map(_.gameStatus.step.toString).getOrElse("No step"))
      )
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

  def isActionDisabled(using view: Signal[ApplicationState])(action: ScatanActions): Signal[Boolean] =
    view.map(_.game.exists(!_.allowedActions.contains(action)))

  def buttonsComponent(using view: Signal[ApplicationState])(using controller: GameController): Element =
    div(
      className := "game-view-buttons",
      button(
        className := "game-view-button roll-dice-button",
        "Roll dice",
        onClick --> { _ => controller.rollDice() },
        disabled <-- isActionDisabled(ScatanActions.RollDice)
      ),
      button(
        className := "game-view-button end-turn-button",
        "End Turn",
        onClick --> { _ => controller.nextTurn() },
        disabled <-- isActionDisabled(ScatanActions.NextTurn)
      )
    )
