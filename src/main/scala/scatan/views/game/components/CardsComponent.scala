package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.model.components.ResourceType
import scatan.model.components.ResourceType.*
import scatan.model.ApplicationState
import scatan.model.components.ResourceCard
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.ScatanState
import scatan.model.components.DevelopmentType
import scatan.model.components.DevelopmentType.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.HTMLDivElement

object CardsComponent:

  extension (state: ScatanState)
    def countResourceOf(player: ScatanPlayer)(resourceType: ResourceType): Int =
      state.resourceCards(player).count(_.resourceType == resourceType)

    def countDevelopmentOf(player: ScatanPlayer)(developmentType: DevelopmentType): Int =
      state.developmentCards(player).count(_.developmentType == developmentType)

  private val resourcesImg: Map[ResourceType, String] = Map(
    Wood -> "res/img/cards/resource/wood.jpg",
    Brick -> "res/img/cards/resource/clay.jpg",
    Sheep -> "res/img/cards/resource/sheep.jpg",
    Wheat -> "res/img/cards/resource/wheat.jpg",
    Rock -> "res/img/cards/resource/ore.jpg"
  )

  private val developmentImg: Map[DevelopmentType, String] = Map(
    Knight -> "res/img/cards/development/knight.png",
    RoadBuilding -> "res/img/cards/development/road-building.png",
    YearOfPlenty -> "res/img/cards/development/year-of-plenty.png",
    Monopoly -> "res/img/cards/development/monopoly.png",
    VictoryPoint -> "res/img/cards/development/victory-point.png"
  )

  private val cardContainerCssClass = "game-view-card-container"
  private val childContainerCssClass = "game-view-child-container"
  private val cardItemCssClass = "game-view-card-item"
  private val cardCountCssClass = "game-view-card-count"
  private val cardCssClass = "game-view-card"

  def cardsComponent(using reactiveState: Signal[ApplicationState]): Element =
    div(
      cls := cardContainerCssClass,
      resourceCardComponent,
      developmentCardComponent
    )

  def resourceCardComponent(using reactiveState: Signal[ApplicationState]): Element =
    div(
      cls := childContainerCssClass,
      for (resourceType, path) <- resourcesImg.toList
      yield div(
        cls := cardItemCssClass,
        div(
          cls := cardCountCssClass,
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

  def developmentCardComponent(using reactiveState: Signal[ApplicationState]): Element =
    div(
      cls := childContainerCssClass,
      for (developmentType, path) <- developmentImg.toList
      yield div(
        cls := cardItemCssClass,
        onClick --> (_ => println("clicked")),
        div(
          cls := cardCountCssClass,
          child.text <-- reactiveState.map(state =>
            (for
              game <- state.game
              currentPlayer = game.turn.player
              resourceCount = game.state.countDevelopmentOf(currentPlayer)(developmentType)
            yield resourceCount).getOrElse(0)
          )
        ),
        cardImageBy(path)
      )
    )

  private def cardImageBy(path: String): Element =
    img(
      cls := cardCssClass,
      src := path
    )
