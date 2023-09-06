package scatan.example.controller

import scatan.example.model.CounterAppState
import scatan.example.view.AboutView
import scatan.mvc.lib.Controller

trait AboutController extends Controller:
  def about(): Unit


class AboutControllerImpl(requirements: Controller.Requirements[AboutView, CounterAppState])
  extends AboutController
    with Controller.Dependencies(requirements):
  override def about(): Unit =
    println("About")
