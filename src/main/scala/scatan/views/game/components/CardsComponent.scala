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

object CardsComponent:

  extension (state: ScatanState)
    def countCardtOf(player: ScatanPlayer)(cardType: CardType): Int = cardType match
      case resourceType: ResourceType =>
        state.resourceCards(player).count(_.resourceType == resourceType)
      case developmentType: DevelopmentType =>
        state.developmentCards(player).count(_.developmentType == developmentType)

  type CardType = ResourceType | DevelopmentType

  private val cardImages: Map[CardType, String] = Map(
    Wood -> "res/img/cards/resource/wood.jpg",
    Brick -> "res/img/cards/resource/clay.jpg",
    Sheep -> "res/img/cards/resource/sheep.jpg",
    Wheat -> "res/img/cards/resource/wheat.jpg",
    Rock -> "res/img/cards/resource/ore.jpg",
    Knight -> "res/img/cards/development/knight.png",
    RoadBuilding -> "res/img/cards/development/road-building.png",
    YearOfPlenty -> "res/img/cards/development/year-of-plenty.png",
    Monopoly -> "res/img/cards/development/monopoly.png",
    VictoryPoint -> "res/img/cards/development/victory-point.png"
  )

  def cardsComponent(using reactiveState: Signal[ApplicationState]): Element =
    div(
      cls := "game-view-card-container",
      cardCountComponent(cardImages.collect { case (k: ResourceType, v) => (k, v) }),
      cardCountComponent(cardImages.collect { case (k: DevelopmentType, v) => (k, v) })
    )

  private def cardCountComponent(using reactiveState: Signal[ApplicationState])(cards: Map[CardType, String]): Element =
    div(
      cls := "game-view-child-container",
      for (cardType, path) <- cards.toList
      yield div(
        cls := "game-view-card-item",
        onClick --> (_ => println("clicked")),
        div(
          cls := "game-view-card-count",
          child.text <-- reactiveState.map(state =>
            (for
              game <- state.game
              currentPlayer = game.turn.player
              resourceCount = game.state.countCardtOf(currentPlayer)(cardType)
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
