package scatan.controllers.setup

import scatan.mvc.lib.Controller
import scatan.views.setup.SetUpView
import scatan.mvc.lib.Model
import scatan.example.model.CounterAppState

trait SetUpController extends Controller

class SetUpControllerImpl(dependencies: Controller.Requirements[SetUpView, CounterAppState]) extends SetUpController
