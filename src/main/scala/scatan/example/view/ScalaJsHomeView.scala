package scatan.example.view

import com.raquo.laminar.api.L.*
import scatan.Pages
import scatan.example.controller.HomeController
import scatan.mvc.lib.{NavigableApplicationManager, ScalaJSView, View}

class ScalaJsHomeView(requirements: View.Requirements[HomeController], container: String)
    extends HomeView
    with View.Dependencies(requirements)
    with ScalaJSView(container):

  private val reactiveCounter = Var(this.controller.counter)

  override def element: Element =
    div(
        h1("Scala.js Home"),
        p("This is a Scala.js view"),
        p("The counter is: ", child.text <-- reactiveCounter.signal),
        button(
          "Increment",
          onClick --> (_ => controller.increment())
        ),
      button(
        "Switch to About Page",
        onClick --> (_ => NavigableApplicationManager.navigateTo(Pages.About))
      )
    )

  override def onCounterUpdated(counter: Int): Unit =
    reactiveCounter.set(counter)
