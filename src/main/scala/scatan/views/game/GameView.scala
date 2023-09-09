package scatan.views.game

import scatan.controllers.game.GameController

import scatan.Pages
import com.raquo.laminar.api.L.*
import scatan.mvc.lib.{ScalaJSView, View}

trait GameView extends View

class ScalaJsGameView(requirements: View.Requirements[GameController], container: String)
    extends GameView
    with View.Dependencies(requirements)
    with ScalaJSView(container):
  val numberOfUsers: Int = 3

  override def element: Element = div(
    h1("Game"),
    p("This is a ScalaJS view"),
    button(
      "Back",
      onClick --> (_ => controller.goToHome())
    )
  )
