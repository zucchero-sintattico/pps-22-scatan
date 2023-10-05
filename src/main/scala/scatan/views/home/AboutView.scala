package scatan.views.home

import com.raquo.laminar.api.L.*
import scatan.Pages
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
    h1("About"),
    p("This is a ScalaJS view"),
    button(
      "Back",
      onClick --> (_ => this.navigateBack())
    )
  )
