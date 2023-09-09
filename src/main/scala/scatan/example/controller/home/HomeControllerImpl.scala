package scatan.example.controller.home

import scatan.example.model.CounterAppState
import scatan.example.view
import scatan.example.view.HomeView
import scatan.example.controller.HomeController
import scatan.mvc.lib.{Controller, Model, ScalaJSView, View}

class HomeControllerImpl(requirements: Controller.Requirements[HomeView, CounterAppState])
    extends HomeController
    with Controller.Dependencies(requirements):

  override def counter: Int = this.model.state.count
  override def increment(): Unit =
    this.model.update { m =>
      m.copy(count = m.count + 1)
    }
    this.view.onCounterUpdated(this.model.state.count)
