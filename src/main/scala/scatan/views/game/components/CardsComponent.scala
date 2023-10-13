package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.model.components.*
import scatan.model.components.DevelopmentType.*
import scatan.model.components.ResourceType.*
import scatan.model.game.*
import scatan.views.game.components.CardContextMap.{CardType, cardImages}
import scatan.views.utils.TypeUtils.*
import scatan.views.viewmodel.ops.ViewModelPlayersOps.cardCountOfCurrentPlayer

object CardContextMap:

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

  /** Display the cards of the current player.
    * @return
    *   the component
    */
  def cardsComponent: DisplayableSource[Element] =
    div(
      cls := "game-view-card-container",
      cardCountComponent(cardImages.collect { case (k: ResourceType, v) => (k, v) }),
      cardCountComponent(cardImages.collect { case (k: DevelopmentType, v) => (k, v) })
    )

  /** Display the given cards with the given images paths.
    * @param cards
    *   the cards to display with their images paths
    * @return
    *   the component
    */
  private def cardCountComponent(cards: Map[CardType, String]): DisplayableSource[Element] =
    div(
      cls := "game-view-child-container",
      for (cardType, path) <- cards.toList
      yield div(
        cls := "game-view-card-item",
        onClick --> { _ => clickHandler.onCardClick(cardType) },
        div(
          cls := "game-view-card-count",
          child.text <-- gameViewModel.cardCountOfCurrentPlayer(cardType).map(_.toString)
        ),
        cardImageBy(path)
      )
    )

  /** Display the card image with the given path.
    * @param path
    *   the path of the image
    * @return
    *   the component
    */
  private def cardImageBy(path: String): Element =
    img(
      cls := "game-view-card",
      src := path
    )
