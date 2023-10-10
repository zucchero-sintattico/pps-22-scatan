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
import scatan.model.game.config.ScatanActions

object RightTabComponent:

  val bankTradeOffer: Var[ResourceType] = Var(ResourceType.values.head)
  val bankTradeRequest: Var[ResourceType] = Var(ResourceType.values.head)
  val playerTradeOffer: Var[Map[ResourceType, Int]] = Var(Map.empty)
  val playerTradeRequest: Var[Map[ResourceType, Int]] = Var(Map.empty)

  def rightTabCssClass: String = "game-view-right-tab"

  def tradeComponent: DisplayableSource[Element] =
    div(
      className := rightTabCssClass,
      h2("Trade:"),
      tradePlayerComponent,
      tradeBankComponent,
      visibility <-- areTradeEnabled.map(if _ then "visible" else "hidden")
    )

  private def areTradeEnabled: Displayable[Signal[Boolean]] =
    reactiveState.map(_.game.map(_.allowedActions.contains(ScatanActions.TradeWithBank)).getOrElse(false))

  private def tradePlayerComponent: DisplayableSource[Element] =
    div(
      h3("Players:"),
      div(
        "Trade ",
        tradePlayerBoardComponent,
        " with "
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

  private def tradeBankComponent: DisplayableSource[Element] =
    div(
      h3("Bank:"),
      div(
        "Trade four of ",
        resourceTypeChoiceComponent(bankTradeOffer),
        " for one of ",
        resourceTypeChoiceComponent(bankTradeRequest),
        button(
          className := "trade-bank-button",
          onClick --> (_ =>
            gameController.onTradeWithBank(
              bankTradeOffer.now(),
              bankTradeRequest.now()
            )
          ),
          "Trade"
        )
      )
    )

  private def resourceTypeChoiceComponent(changing: Var[ResourceType]): InputSource[Element] =
    div(
      className := "game-view-resource-type-choice",
      select(
        onChange.mapToValue.map(ResourceType.withName(_)) --> changing,
        className := "game-view-resource-type-choice-select",
        // for each type of resource add an option
        for resource <- ResourceType.values
        yield option(resource.toString, value := resource.toString)
      )
    )

  private def getPlayersList(game: ScatanGame): InputSource[Element] =
    ul(
      for player <- game.players.filterNot(_.name == game.turn.player.name)
      yield li(
        className := "trade-player",
        player.name,
        button(
          className := "trade-player-button",
          onClick --> (_ =>
            gameController.onTradeWithPlayer(
              player,
              playerTradeOffer.now(),
              playerTradeRequest.now()
            )
          ),
          "Trade"
        )
      )
    )

  private def tradePlayerBoardComponent: InputSource[Element] =
    div(
      for resourceType <- ResourceType.values
      yield div(
        className := "game-view-resource-type-choice",
        tradeAmountComponent(playerTradeOffer, resType = resourceType),
        " of ",
        label(resourceType.toString),
        br()
      ),
      "for",
      br(),
      div(
        for resourceType <- ResourceType.values
        yield div(
          className := "game-view-resource-type-choice",
          tradeAmountComponent(playerTradeRequest, resType = resourceType),
          " of ",
          label(resourceType.toString),
          br()
        )
      )
    )

  private def tradeAmountComponent(
      reactiveRef: Var[Map[ResourceType, Int]],
      resType: ResourceType
  ): InputSource[Element] =
    input(
      className := "game-view-resource-amount",
      `type` := "number",
      minAttr := "0",
      defaultValue := "0",
      placeholder := "Amount",
      onChange.mapToValue.map(_.toInt) --> (amount =>
        reactiveRef.update(_ + (resType -> amount))
        println(reactiveRef.now().orElse(Map.empty))
      )
    )
