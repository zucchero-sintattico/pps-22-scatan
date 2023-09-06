package scatan.controllers.setup

import scatan.mvc.lib.Controller
import scatan.views.setup.SetUpView

trait SetUpController extends Controller

class SetUpControllerImpl(dependencies: Controller.Requirements[SetUpView, ?]) extends SetUpController
