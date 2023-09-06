package scatan.views.home

import com.raquo.laminar.api.L.*
import scatan.mvc.lib.{ScalaJSView, View}
import scatan.controllers.home.AboutController

trait AboutView extends View

class ScalaJSAboutView(requirements: View.Requirements[AboutController], container: String)
    extends AboutView
    with View.Dependencies[AboutController](requirements)
    with ScalaJSView(container):

  override def element: Element = div(
    h1("About"),
    p("This is a ScalaJS view"),
    button(
      "Back",
      onClick --> (_ => controller.goToHome())
    )
  )
