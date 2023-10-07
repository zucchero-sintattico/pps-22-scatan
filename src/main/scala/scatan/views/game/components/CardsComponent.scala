package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.model.components.ResourceType
import scatan.model.components.ResourceType.*
import scatan.model.ApplicationState
import scatan.model.components.ResourceCard
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.ScatanState

object CardsComponent:

  extension (state: ScatanState)
    def countResourceOf(player: ScatanPlayer)(resourceType: ResourceType): Int =
      state.resourceCards(player).count(_.resourceType == resourceType)

  private val resourcesImg: Map[ResourceType, String] = Map(
    Wood -> "res/img/cards/wood.jpg",
    Brick -> "res/img/cards/clay.jpg",
    Sheep -> "res/img/cards/sheep.jpg",
    Wheat -> "res/img/cards/wheat.jpg",
    Rock -> "res/img/cards/ore.jpg"
  )

  def resourceCardComponent(using reactiveState: Signal[ApplicationState]): Element =
    div(
      cls := "game-view-card-container",
      for (resourceType, path) <- resourcesImg.toList
      yield div(
        cls := "game-view-card-item",
        div(
          cls := "game-view-card-count",
          child.text <-- reactiveState.map(state =>
            (for
              game <- state.game
              currentPlayer = game.turn.player
              resourceCount = game.state.countResourceOf(currentPlayer)(resourceType)
            yield resourceCount).getOrElse(0)
          )
        ),
        cardImageBy(path)
      )
    )

  private def cardImageBy(path: String): Element =
    img(
      cls := "game-view-card",
      src := path
    )
