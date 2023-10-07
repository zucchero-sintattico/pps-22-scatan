package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.model.components.ResourceType
import scatan.model.components.ResourceType.*

object CardsComponent:

  private val resourcesImg: Map[ResourceType, String] = Map(
    Wood -> "res/img/cards/wood.jpg",
    Brick -> "res/img/cards/clay.jpg",
    Sheep -> "res/img/cards/sheep.jpg",
    Wheat -> "res/img/cards/wheat.jpg",
    Rock -> "res/img/cards/ore.jpg"
  )

  def resourceCardComponent: Element =
    div(
      cls := "game-view-card-container",
      for (resource, path) <- resourcesImg.toList
      yield div(
        cls := "game-view-card-item",
        div(
          cls := "game-view-card-count",
          "0"
        ),
        imageComponent(path)
      )
    )

  private def imageComponent(path: String): Element =
    img(
      cls := "game-view-card",
      src := path
    )
