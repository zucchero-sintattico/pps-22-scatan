package scatan.views.game

import com.raquo.laminar.api.L.*
import scatan.controllers.game.GameController
import scatan.lib.mvc.{BaseScalaJSView, View}
import scatan.model.ApplicationState
import scatan.views.game.components.{CardsComponent, GameMapComponent, LeftTabComponent}
import scatan.views.game.components.AwardsComponent

trait GameView extends View[ApplicationState]

object GameView:
  def apply(container: String, requirements: View.Requirements[GameController]): GameView =
    ScalaJsGameView(container, requirements)

private class ScalaJsGameView(container: String, requirements: View.Requirements[GameController])
    extends BaseScalaJSView[ApplicationState, GameController](container, requirements)
    with GameView:

  given Signal[ApplicationState] = this.reactiveState
  given GameController = this.controller
  override def element: Element =
    div(
      div(
        className := LeftTabComponent.leftTabCssClass,
        AwardsComponent.awardsComponent,
        LeftTabComponent.currentPlayerComponent,
        LeftTabComponent.buttonsComponent,
        LeftTabComponent.possibleMovesComponent
      ),
      GameMapComponent.mapComponent,
      CardsComponent.cardsComponent
    )
