package scatan.views.home

import com.raquo.laminar.api.L.*
import scatan.controllers.home.AboutController
import scatan.lib.mvc.{BaseScalaJSView, View}
import scatan.model.ApplicationState

trait AboutView extends View[ApplicationState]

object AboutView:
  def apply(container: String, requirements: View.Requirements[AboutController]): AboutView =
    ScalaJSAboutView(container, requirements)

private class ScalaJSAboutView(container: String, requirements: View.Requirements[AboutController])
    extends BaseScalaJSView[ApplicationState, AboutController](container, requirements)
    with AboutView:

  override def element: Element = div(
    cls := "about-view-container",
    h1("About"),
    p(
      "The goal of the project is to create a clone of the board game 'Settlers of Catan', a game for 3 or 4 players. "
    ),
    p(
      "In the game, each participant takes on the role of a settler trying to establish themselves on the island of Catan. "
    ),
    p(
      "The game is played on a board consisting of hexagonal tiles, which are arranged randomly at the beginning of the game. "
    ),
    p(
      "The primary objective is to accumulate essential resources, including wood, clay, wheat, wool, and ore, through the construction of strategically placed settlements, cities, and roads on the island. These resources are obtained based on the results of dice rolls and the location of the structures built."
    ),
    h2("Built with"),
    ul(
      li("Scala 3"),
      li("Scala.js"),
      li("Laminar"),
      li("ScalaTest")
    ),
    h2("Authors"),
    ul(
      // add links to github profiles
      li(
        a(
          cls := "about-link",
          "Luigi Borriello",
          href := "https://github.com/luigi-borriello00"
        )
      ),
      li(
        a(
          cls := "about-link",
          "Manuel Andruccioli",
          href := "https://github.com/manuandru"
        )
      ),
      li(
        a(cls := "about-link", "Alessandro Mazzoli", href := "https://github.com/alemazzo")
      )
    ),
    button(
      cls := "home-menu-button",
      "Back",
      onClick --> (_ => this.navigateBack())
    )
  )
