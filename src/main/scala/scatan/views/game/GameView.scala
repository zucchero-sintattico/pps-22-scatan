package scatan.views.game

import scatan.controllers.game.GameController
import scatan.Pages
import com.raquo.laminar.api.L.*
import scatan.lib.mvc.{BaseScalaJSView, View}
import scatan.model.map.Spot
import scatan.model.map.Hexagon
import scatan.model.GameMap
import scatan.views.game.components.GameMapComponent.getMapComponent

trait GameView extends View

object GameView:
  def apply(container: String, requirements: View.Requirements[GameController]): GameView =
    ScalaJsGameView(container, requirements)

private class ScalaJsGameView(container: String, requirements: View.Requirements[GameController])
    extends BaseScalaJSView(container, requirements)
    with GameView:

  val gameMap = GameMap()

  override def element: Element =
    div(
      display := "block",
      width := "50%",
      margin := "auto",
      getMapComponent(gameMap)
    )
