package scatan.views.game

import com.raquo.laminar.api.L.*
import scatan.lib.mvc.{BaseScalaJSView, Model, View}
import scatan.model.{ApplicationState, GameMap}
import scatan.views.game.components.GameMapComponent.getMapComponent

trait GameView extends View[ApplicationState]

object GameView:
  def apply(container: String, requirements: View.Requirements[GameController]): GameView =
    ScalaJsGameView(container, requirements)

private class ScalaJsGameView(container: String, requirements: View.Requirements[GameController])
    extends BaseScalaJSView[ApplicationState, GameController](container, requirements)
    with GameView:

  val gameMap = GameMap()

  import com.raquo.laminar.api.features.unitArrows
  override def element: Element =
    div(
      display := "block",
      width := "50%",
      margin := "auto",
      h1(
        child.text <-- this.reactiveState
          .map("Current Player: " + _.game.map(_.turn.player.name).getOrElse("No player"))
      ),
      button(
        "End Turn",
        onClick --> this.controller.nextTurn()
      ),
      getMapComponent(gameMap)
    )
