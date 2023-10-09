package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.model.ApplicationState
import scatan.model.components.ResourceType
import scatan.controllers.game.GameController
import scatan.model.game.ScatanState
import scatan.model.game.ScatanGame
import scatan.model.game.config.ScatanPhases
import scatan.views.utils.TypeUtils.DisplayableSource
import scatan.views.utils.TypeUtils.{reactiveState, gameController}
import scatan.views.utils.TypeUtils.Displayable
import scatan.views.utils.TypeUtils.InputSource

object RightTabComponent:

  def rightTabCssClass: String = "game-view-right-tab"

  def tradeComponent: DisplayableSource[Element] =
    div(
      className := rightTabCssClass,
      h2("Trade:"),
      tradePlayerComponent
      // tradeBankComponent
      // visibility <-- areTradeEnabled.map(if _ then "visible" else "hidden")
    )

  private def tradePlayerComponent: DisplayableSource[Element] =
    div(
      h3("Players:"),
      div(
        "Trade ",
        resourceTypeChoiceComponent,
        tradeAmountComponent,
        " for ",
        resourceTypeChoiceComponent,
        tradeAmountComponent
      ),
      div(
        className := "game-view-players",
        child <-- reactiveState
          .map(state =>
            (for game <- state.game
            yield getPlayersList(game)).getOrElse(div("No game"))
          )
      )
    )

  private def areTradeEnabled: Displayable[Signal[Boolean]] =
    reactiveState.map(_.game.exists(_.gameStatus.phase == ScatanPhases.Game))

  private def getPlayersList(game: ScatanGame): InputSource[Element] =
    ul(
      for player <- game.players.filterNot(_.name == game.turn.player.name)
      yield li(
        className := "trade-player",
        player.name,
        button(
          className := "trade-player-button",
          "Trade"
        )
      )
    )

  private def resourceTypeChoiceComponent: InputSource[Element] =
    div(
      className := "game-view-resource-type-choice",
      select(
        className := "game-view-resource-type-choice-select",
        // for each type of resource add an option
        for resource <- ResourceType.values
        yield option(resource.toString, value := resource.toString)
      )
    )

  private def tradeAmountComponent: InputSource[Element] =
    input(
      className := "game-view-resource-amount",
      `type` := "number",
      minAttr := "0",
      placeholder := "Amount"
    )
