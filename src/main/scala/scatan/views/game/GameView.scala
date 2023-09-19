package scatan.views.game

import scatan.controllers.game.GameController

import scatan.Pages
import com.raquo.laminar.api.L.*
import scatan.lib.mvc.{ScalaJSView, View}
import scatan.model.Spot
import scatan.model.map.Hexagon
import scatan.model.GameMap
import scatan.views.game.components.GameMapComponent.getMapComponent
import scatan.lib.mvc.BaseView
import scatan.lib.mvc.BaseScalaJSView

trait GameView extends View

object GameView:
  def apply(container: String, requirements: View.Requirements[GameController]): GameView =
    ScalaJsGameView(container, requirements)

private class ScalaJsGameView(container: String, requirements: View.Requirements[GameController])
    extends BaseScalaJSView(container, requirements)
    with GameView:

  given hexSize: Int = 100
  val gameMap = GameMap(2)

  override def element: Element =
    div(
      display := "block",
      width := "70%",
      margin := "auto",
      getMapComponent(gameMap)
    )
