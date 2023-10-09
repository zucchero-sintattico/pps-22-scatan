package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.model.ApplicationState
import scatan.controllers.game.GameController
import scatan.model.game.ScatanState
import scatan.model.game.ScatanGame

object RightTabComponent:

  def rightTabCssClass: String = "game-view-right-tab"

  def playerListComponent(using reactiveState: Signal[ApplicationState])(using
      gameController: GameController
  ): Element =
    div(
      className := "game-view-player-list",
      h2("Trade:"),
      h3("Players:"),
      div(
        className := "game-view-players",
        child <-- reactiveState
          .map(state =>
            (for game <- state.game
            yield getPlayersList(game)).getOrElse(div("No game"))
          )
      )
    )

  private def getPlayersList(game: ScatanGame)(using gameController: GameController): Element =
    ul(
      for player <- game.players.filterNot(_.name == game.turn.player.name)
      yield li(
        className := "trade-player",
        player.name,
        button(
          className := "trade-button",
          "Trade"
          //   onClick.mapTo(player) --> ??? // gameController.tradeWithPlayer
        )
      )
    )
