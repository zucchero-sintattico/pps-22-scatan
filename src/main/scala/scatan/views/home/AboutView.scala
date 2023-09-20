package scatan.views.home

import com.raquo.laminar.api.L.*
import scatan.lib.mvc.{ScalaJSView, View}
import scatan.controllers.home.AboutController
import scatan.lib.mvc.{View, BaseScalaJSView}
import scatan.Pages

trait AboutView extends View

object AboutView:
  def apply(container: String, requirements: View.Requirements[AboutController]): AboutView =
    ScalaJSAboutView(container, requirements)

private class ScalaJSAboutView(container: String, requirements: View.Requirements[AboutController])
    extends BaseScalaJSView(container, requirements)
    with AboutView:

  override def element: Element = div(
    h1("About"),
    p("This is a ScalaJS view"),
    button(
      "Back",
      onClick --> (_ => this.navigateBack())
    )
  )
