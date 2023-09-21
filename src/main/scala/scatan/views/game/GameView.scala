package scatan.views.game

import scatan.controllers.game.GameController

import scatan.Pages
import com.raquo.laminar.api.L.*
import scatan.lib.mvc.{ScalaJSView, View}
import scatan.model.map.Spot
import scatan.model.map.Hexagon
import scatan.model.GameMap
import scatan.views.game.components.GameMapComponent.getMapComponent

trait GameView extends View

class ScalaJsGameView(requirements: View.Requirements[GameController], container: String)
    extends GameView
    with View.Dependencies(requirements)
    with ScalaJSView(container):

  val gameMap = GameMap()

  override def element: Element =
    div(
      display := "block",
      width := "50%",
      margin := "auto",
      getMapComponent(gameMap)
    )
