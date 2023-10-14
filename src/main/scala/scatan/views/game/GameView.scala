package scatan.views.game

import com.raquo.laminar.api.L.*
import scatan.controllers.game.GameController
import scatan.lib.mvc.{BaseScalaJSView, View}
import scatan.model.ApplicationState
import scatan.model.game.config.ScatanPhases
import scatan.views.game.components.*
import scatan.views.utils.TypeUtils
import scatan.views.viewmodel.ScatanViewModel

trait GameView extends View[ApplicationState]

object GameView:
  def apply(container: String, requirements: View.Requirements[GameController]): GameView =
    ScalaJsGameView(container, requirements)

private class ScalaJsGameView(container: String, requirements: View.Requirements[GameController])
    extends BaseScalaJSView[ApplicationState, GameController](container, requirements)
    with GameView:

  given ScatanViewModel = ScatanViewModel(this.reactiveState)
  given GameViewClickHandler = GameViewClickHandler(this, controller)

  override def element: Element =
    div(
      DevelopmentCardPopups.All,
      EndgameComponent.endgamePopup,
      div(
        className := LeftTabComponent.leftTabCssClass,
        LeftTabComponent.awardsComponent,
        LeftTabComponent.currentPlayerComponent,
        LeftTabComponent.buttonsComponent,
        LeftTabComponent.possibleMovesComponent
      ),
      GameMapComponent.mapComponent,
      RightTabComponent.tradeComponent,
      CardsComponent.cardsComponent
    )
