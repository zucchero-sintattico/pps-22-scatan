package scatan.views.game

import scatan.controllers.game.GameController
import scatan.Pages
import com.raquo.laminar.api.L.*
import scatan.lib.mvc.{BaseScalaJSView, Model, View}
import scatan.model.{ApplicationState, GameMap}
import com.raquo.laminar.modifiers.RenderableNode
import com.raquo.laminar.nodes.ChildNode.Base
import scatan.model.game.ScatanPhases
import scatan.model.game.ScatanActions
import scatan.views.game.components.GameMapComponent
import scatan.views.game.components.LeftTabComponent
import scatan.views.game.components.CardsComponent

trait GameView extends View[ApplicationState]

object GameView:
  def apply(container: String, requirements: View.Requirements[GameController]): GameView =
    ScalaJsGameView(container, requirements)

private class ScalaJsGameView(container: String, requirements: View.Requirements[GameController])
    extends BaseScalaJSView[ApplicationState, GameController](container, requirements)
    with GameView:

  given Signal[ApplicationState] = this.reactiveState
  given GameController = this.controller

  import com.raquo.laminar.api.features.unitArrows
  override def element: Element =
    div(
      div(
        className := LeftTabComponent.leftTabCssClass,
        LeftTabComponent.currentPlayerComponent,
        LeftTabComponent.buttonsComponent,
        LeftTabComponent.possibleMovesComponent
      ),
      GameMapComponent.mapComponent,
      CardsComponent.resourceCardComponent
    )
