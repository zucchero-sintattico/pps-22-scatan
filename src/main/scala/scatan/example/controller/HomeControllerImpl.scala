package scatan.example.controller

import scatan.example.model.CounterAppState
import scatan.example.view
import scatan.example.view.HomeView
import scatan.mvc.lib.{Controller, Model, ScalaJS, View}

class HomeControllerImpl(requirements: Controller.Requirements[HomeView, CounterAppState])
    extends HomeController
    with Controller.Dependencies(requirements):
  override def increment(): Unit =
    this.model.update { m =>
      m.copy(count = m.count + 1)
    }
