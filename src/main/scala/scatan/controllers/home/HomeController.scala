package scatan.controllers.home

import scatan.mvc.lib.Controller
import scatan.views.home.HomeView
import scatan.Pages
import scatan.mvc.lib.application.NavigableApplication
import scatan.mvc.lib.NavigableApplicationManager

trait HomeController extends Controller:
  def goToSetup(): Unit
  def goToAbout(): Unit

class HomeControllerImpl(requirements: Controller.Requirements[HomeView, ?]) extends HomeController:
  override def goToSetup(): Unit =
    NavigableApplicationManager.navigateTo[Pages](Pages.Setup)

  override def goToAbout(): Unit =
    NavigableApplicationManager.navigateTo[Pages](Pages.About)
