package scatan.views.home

import scatan.controllers.home.HomeController
import com.raquo.laminar.api.L.*
import scatan.Pages
import scatan.lib.mvc.{BaseScalaJSView, View}

/** This is the view for the home page.
  */
trait HomeView extends View

object HomeView:
  def apply(container: String, requirements: View.Requirements[HomeController]): HomeView =
    ScalaJsHomeView(container, requirements)

/** This is the view for the home page.
  *
  * @param requirements,
  *   the requirements for the view
  * @param container,
  *   the container for the view
  */
private class ScalaJsHomeView(container: String, requirements: View.Requirements[HomeController])
    extends BaseScalaJSView(container, requirements)
    with HomeView:

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
          onClick --> (_ => this.navigateTo(Pages.SetUp)),
          "Play"
        ),
        button(
          cls := "home-menu-button",
          onClick --> (_ => this.navigateTo(Pages.About)),
          "About"
        )
      )
    )
