package scatan.example.view

import com.raquo.laminar.api.L
import scatan.example.controller.HomeController
import scatan.mvc.lib.{ScalaJS, View}

class ScalaJsHomeView(requirements: View.Requirements[HomeController], container: String)
    extends HomeView
    with View.Dependencies(requirements)
    with ScalaJS(container):

  override def element: L.Element =
    ???
  override def onCounterUpdated(counter: Int): Unit =
    ???
