package scatan.views.game

import scatan.controllers.game.GameController

import scatan.Pages
import com.raquo.laminar.api.L.*
import scatan.mvc.lib.{ScalaJSView, View}
import scatan.model.Spot
import scatan.model.map.Hexagon
import scatan.model.GameMap
import scatan.views.game.components.GameMapComponent
import scatan.views.game.components.GameMapComponent.getMapComponent

trait GameView extends View

class ScalaJsGameView(requirements: View.Requirements[GameController], container: String)
    extends GameView
    with View.Dependencies(requirements)
    with ScalaJSView(container):

  given hexSize: Int = 100
  val gameMap = GameMap(2)

  override def element: Element =
    div(
      display := "block",
      width := "70%",
      margin := "auto",
      getMapComponent(gameMap)
    )
