package scatan.views.home

import scatan.controllers.home.HomeController
import com.raquo.laminar.api.L.*
import scatan.Pages

import scatan.mvc.lib.{NavigableApplicationManager, ScalaJSView, View}

/** This is the view for the home page.
  *
  * @param requirements,
  *   the requirements for the view
  * @param container,
  *   the container for the view
  */
class ScalaJsHomeView(requirements: View.Requirements[HomeController], container: String)
    extends HomeView
    with View.Dependencies(requirements)
    with ScalaJSView(container):

  def start(): Unit =
    print("Hello, world!")

  override def element: Element =
    div(
      cls := "home-view",
      // Title
      div(
        cls := "home-title"
      ),
      // Menu view with 3 buttons, play, settings and about, dispose them vertically
      div(
        cls := "home-menu",
        button(
          cls := "home-menu-button",
          onClick --> (_ => controller.goToSetup()),
          "Play"
        ),
        button(
          cls := "home-menu-button",
          onClick --> (_ => controller.goToAbout()),
          "About"
        )
      )
    )
