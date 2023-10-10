package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.controllers.game.GameController
import scatan.model.ApplicationState
import scatan.model.components.DevelopmentType.*
import scatan.model.components.{DevelopmentType, ResourceCard, ResourceType}
import scatan.model.components.ResourceType.*
import scatan.model.game.ScatanState
import scatan.model.game.config.ScatanPlayer
import scatan.views.game.components.CardContextMap.{CardType, cardImages, countCardOf}

object CardContextMap:
  extension (state: ScatanState)
    def countCardOf(player: ScatanPlayer)(cardType: CardType): Int = cardType match
      case resourceType: ResourceType =>
        state.resourceCards(player).count(_.resourceType == resourceType)
      case developmentType: DevelopmentType =>
        state.developmentCards(player).count(_.developmentType == developmentType)

  type CardType = ResourceType | DevelopmentType

  val cardImages: Map[CardType, String] = Map(
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

object CardsComponent:
  def cardsComponent(using reactiveState: Signal[ApplicationState])(using gameController: GameController): Element =
    div(
      cls := "game-view-card-container",
      cardCountComponent(cardImages.collect { case (k: ResourceType, v) => (k, v) }),
      cardCountComponent(cardImages.collect { case (k: DevelopmentType, v) => (k, v) })
    )

  private def cardCountComponent(using reactiveState: Signal[ApplicationState])(using gameController: GameController)(
      cards: Map[CardType, String]
  ): Element =
    div(
      cls := "game-view-child-container",
      for (cardType, path) <- cards.toList
      yield div(
        cls := "game-view-card-item",
        onClick --> (_ => gameController.clickCard(cardType)),
        div(
          cls := "game-view-card-count",
          child.text <-- reactiveState.map(state =>
            (for
              game <- state.game
              currentPlayer = game.turn.player
              resourceCount = game.state.countCardOf(currentPlayer)(cardType)
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
