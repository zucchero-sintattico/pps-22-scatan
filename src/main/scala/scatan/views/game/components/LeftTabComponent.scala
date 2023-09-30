package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.controllers.game.GameController
import scatan.views.game.GameView
import scatan.lib.mvc.ScalaJSView
import scatan.model.ApplicationState
import scatan.model.game.ScatanPhases
import scatan.views.game.InitialAssignmentView
import scatan.controllers.game.InitialAssignmentController

object LeftTabComponent:

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
            for move <- state.game.map(_.phase.possibleMoves).getOrElse(Seq.empty)
            yield li(cls := "game-view-move", move)
          )
      )
    )

  extension (phase: ScatanPhases)
    def possibleMoves: Seq[String] = phase match
      case ScatanPhases.InitialRoadAssignment      => Seq("Build Road")
      case ScatanPhases.InitialSettlmentAssignment => Seq("Build Settlement")
      case ScatanPhases.Initial                    => Seq("Roll Dice")
      case ScatanPhases.RobberPlacement            => Seq("Place Robber")
      case ScatanPhases.CardStealing               => Seq("Steal Card")
      case ScatanPhases.Playing =>
        Seq(
          "Build City",
          "Build Road",
          "Build Settlement",
          "Buy development Card",
          "Play development Card",
          "Trade with Bank",
          "Trade with Player",
          "End turn"
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
