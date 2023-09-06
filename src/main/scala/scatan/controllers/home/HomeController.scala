package scatan.controllers

import scatan.mvc.lib.Controller
import scatan.views.HomeView
import scatan.example.model.CounterAppState

trait HomeController extends Controller

class HomeControllerImpl(requirements: Controller.Requirements[HomeView, CounterAppState]) extends HomeController:
  def start(): Unit =
    print("Hello, world!")
