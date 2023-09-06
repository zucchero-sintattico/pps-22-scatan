package scatan.views.setup

import scatan.controllers.setup.SetUpController

import scatan.Pages
import scatan.mvc.lib.View

/** This is the view for the setup page.
  */
trait SetUpView extends View:
  def start(): Unit
