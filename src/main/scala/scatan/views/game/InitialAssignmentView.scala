package scatan.views.game

import com.raquo.laminar.api.L.*
import com.raquo.laminar.modifiers.RenderableNode
import com.raquo.laminar.nodes.ChildNode.Base
import scatan.Pages
import scatan.controllers.game.{GameController, InitialAssignmentController, PositioningHandler}
import scatan.lib.mvc.application.Application
import scatan.lib.mvc.{BaseScalaJSView, Model, View}
import scatan.model.{ApplicationState, GameMap}
import scatan.views.game.components.{CardsComponent, GameMapComponent, LeftTabComponent}

trait InitialAssignmentView extends View[ApplicationState]

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
