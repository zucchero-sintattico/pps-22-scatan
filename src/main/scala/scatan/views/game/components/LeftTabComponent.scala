package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.model.components.Award
import scatan.model.game.config.ScatanActions
import scatan.views.utils.TypeUtils.*
import scatan.views.viewmodel.ops.ViewModelActionsOps.*
import scatan.views.viewmodel.ops.ViewModelCurrentStatusOps.*
import scatan.views.viewmodel.ops.ViewModelPlayersOps.*
import scatan.model.game.state.ScatanState

object LeftTabComponent:

  def leftTabCssClass: String = "game-view-left-tab"

  /** Displays the current player, their score, and the current phase and step of the game.
    * @return
    *   the component
    */
  def currentPlayerComponent: Displayable[Element] =
    div(
      h2(
        className := "game-view-player",
        child.text <-- gameViewModel.currentPlayer.map("Current Player: " + _.name)
      ),
      h2(
        className := "game-view-player-score",
        child.text <-- gameViewModel.currentPlayerScore.map("Score: " + _)
      ),
      h2(
        className := "game-view-phase",
        child.text <-- gameViewModel.currentPhase.map("Phase: " + _)
      ),
      h2(
        className := "game-view-step",
        child.text <-- gameViewModel.currentStep.map("Step: " + _)
      )
    )

  def playerColorComponent: GameStateKnowledge[Element] =
    div(
      className := "game-view-player-color",
      for
        (player, count) <- summon[ScatanState].players.zipWithIndex
        playerCssClass = s"player-${count + 1}"
      yield p(
        className := playerCssClass,
        player.name
      )
    )

  extension (scatanAction: ScatanActions)
    private def toDisplayable: String =
      scatanAction.toString.split("(?=[A-Z])").map(_.capitalize).mkString(" ")

  /** Displays all the actions the current player can take.
    * @return
    *   the component
    */
  def possibleMovesComponent: Displayable[Element] =
    div(
      className := "game-view-moves",
      "Possible moves:",
      ul(
        children <-- gameViewModel.allowedActions.split(_.ordinal) { (_, action, _) =>
          li(cls := "game-view-move", action.toDisplayable)
        }
      )
    )

  /** Displays the buttons to perform actions.
    * @return
    *   the component
    */
  def buttonsComponent: DisplayableSource[Element] =
    div(
      div(
        className := "game-view-buttons",
        button(
          className := "game-view-button roll-dice-button",
          "Roll dice",
          disabled <-- gameViewModel.isActionDisabled(ScatanActions.RollDice),
          onClick --> { _ => clickHandler.onRollDiceClick() }
        ),
        button(
          className := "game-view-button buy-development-card-button",
          "Buy Dev. Card",
          onClick --> { _ => clickHandler.onBuyDevelopmentCardClick() },
          disabled <-- gameViewModel.canBuyDevelopment
        ),
        button(
          className := "game-view-button end-turn-button",
          "End Turn",
          disabled <-- gameViewModel.isActionDisabled(ScatanActions.NextTurn),
          onClick --> { _ => clickHandler.onEndTurnClick() }
        )
      ),
      div(
        className := "game-view-buttons",
        StealCardPopup.userSelectionPopup(),
        button(
          className := "game-view-button steal-card-button",
          "Steal Card",
          onClick --> { _ => StealCardPopup.show() },
          disabled <-- gameViewModel.isActionDisabled(ScatanActions.StealCard)
        )
      )
    )

  /** Displays the awards that have been given out so far, if any.
    * @return
    *   the component
    */
  def awardsComponent: DisplayableSource[Element] =
    div(
      className := "awards",
      h2("Current awards"),
      div(
        ul(
          children <-- gameViewModel.currentAwards.map(_.toSeq).split(_._1) { (award, opt, _) =>
            li(
              award.toDisplayable,
              opt._2.map((player, score) => s" ($player: $score)").getOrElse("Nobody Yet")
            )
          }
        )
      )
    )

  extension (award: Award)
    private def toDisplayable: String =
      award.awardType.toString.split("(?=[A-Z])").map(_.capitalize).mkString(" ")
