package scatan.example.view

import com.raquo.laminar.api.L.*
import scatan.example.controller.AboutController
import scatan.mvc.lib.{NavigableApplicationManager, ScalaJSView, View}

trait AboutView extends View:
  def about(): Unit

class ScalaJSAboutView(requirements: View.Requirements[AboutController], container: String)
    extends AboutView
    with View.Dependencies[AboutController](requirements)
    with ScalaJSView(container):

  override def about(): Unit = ???

  override def element: Element = div(
    h1("About"),
    p("This is a ScalaJS view"),
    button(
      "Back",
      onClick --> (_ => NavigableApplicationManager.navigateBack())
    )
  )
