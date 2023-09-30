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
import scatan.views.game.components.LeftTabComponent
import scatan.lib.mvc.application.Application
import scatan.controllers.game.PositioningHandler
import scatan.views.game.components.GameMapComponent
import scatan.controllers.game.InitialAssignmentController

trait InitialAssignmentView extends View[ApplicationState]:
  def switchToGame(): Unit

object InitialAssignmentView:
  def apply(container: String, requirements: View.Requirements[InitialAssignmentController]): InitialAssignmentView =
    ScalaJsInitialAssignmentView(container, requirements)

private class ScalaJsInitialAssignmentView(
    container: String,
    requirements: View.Requirements[InitialAssignmentController]
) extends BaseScalaJSView[ApplicationState, InitialAssignmentController](container, requirements)
    with InitialAssignmentView:

  given Signal[ApplicationState] = this.reactiveState
  given PositioningHandler = this.controller

  override def switchToGame(): Unit =
    this.navigateTo(Pages.Game)

  import com.raquo.laminar.api.features.unitArrows
  override def element: Element =
    div(
      div(
        className := LeftTabComponent.leftTabCssClass,
        LeftTabComponent.currentPlayerComponent,
        LeftTabComponent.possibleMovesComponent
      ),
      GameMapComponent.mapComponent
    )
