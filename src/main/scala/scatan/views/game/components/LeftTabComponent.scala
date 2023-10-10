package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.controllers.game.GameController
import scatan.lib.mvc.ScalaJSView
import scatan.model.ApplicationState
import scatan.model.game.config.ScatanActions
import scatan.model.game.ops.ScoreOps.scores
import scatan.views.game.GameView
import scatan.model.game.ScatanState
import scatan.model.components.Award
import scatan.model.components.AwardType
import scatan.model.game.ops.AwardOps.awards
import scatan.views.utils.TypeUtils.{Displayable, DisplayableSource}
import scatan.views.utils.TypeUtils.{gameController, reactiveState}

object LeftTabComponent:

  extension (action: ScatanActions) def toViewAction: String = action.toString

  def leftTabCssClass: String = "game-view-left-tab"

  def currentPlayerComponent: Displayable[Element] =
    div(
      h2(
        className := "game-view-player",
        child.text <-- reactiveState
          .map("Current Player: " + _.game.map(_.turn.player.name).getOrElse("No player"))
      ),
      h2(
        className := "game-view-player-score",
        child.text <-- reactiveState
          .map("Score: " + _.game.map(game => game.state.scores(game.turn.player)).getOrElse("No score"))
      ),
      h2(
        className := "game-view-phase",
        child.text <-- reactiveState
          .map("Phase: " + _.game.map(_.gameStatus.phase.toString).getOrElse("No phase"))
      ),
      h2(
        className := "game-view-step",
        child.text <-- reactiveState
          .map("Step: " + _.game.map(_.gameStatus.step.toString).getOrElse("No step"))
      )
    )

  def possibleMovesComponent: Displayable[Element] =
    div(
      className := "game-view-moves",
      "Possible moves:",
      ul(
        children <-- reactiveState
          .map(state =>
            for move <- state.game.map(_.allowedActions.toSeq).getOrElse(Seq.empty)
            yield li(cls := "game-view-move", move.toViewAction)
          )
      )
    )

  def isActionDisabled(action: ScatanActions): Displayable[Signal[Boolean]] =
    reactiveState.map(_.game.exists(!_.allowedActions.contains(action)))

  def buttonsComponent: DisplayableSource[Element] =
    div(
      className := "game-view-buttons",
      button(
        className := "game-view-button roll-dice-button",
        "Roll dice",
        onClick --> { _ => gameController.rollDice() },
        disabled <-- isActionDisabled(ScatanActions.RollDice)
      ),
      button(
        className := "game-view-button buy-development-card-button",
        "Buy Dev. Card",
        onClick --> { _ => gameController.buyDevelopmentCard() },
        disabled <-- isActionDisabled(ScatanActions.BuyDevelopmentCard)
      ),
      button(
        className := "game-view-button end-turn-button",
        "End Turn",
        onClick --> { _ => gameController.nextTurn() },
        disabled <-- isActionDisabled(ScatanActions.NextTurn)
      )
    )

  def awardsComponent: DisplayableSource[Element] =
    div(
      className := "awards",
      h2("Current awards"),
      child <-- reactiveState
        .map(state =>
          (for
            game <- state.game
            gameState = game.state
          yield getCurrentAwards(gameState))
            .getOrElse(div("No game"))
        )
    )
  private def getCurrentAwards(state: ScatanState): Element =
    div(
      ul(
        li(
          "Longest road: ",
          state
            .awards(Award(AwardType.LongestRoad))
            .map(award => s"${award._1.name} (${award._2} roads)")
            .getOrElse("Nobody yet")
        ),
        li(
          "Largest army: ",
          state
            .awards(Award(AwardType.LargestArmy))
            .map(award => s"${award._1.name} (${award._2} cards)")
            .getOrElse("Nobody yet")
        )
      )
    )
