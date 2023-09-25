package scatan.lib.mvc

trait View:
  def show(): Unit
  def hide(): Unit

/** The View object.
  */
object View:
  type Factory[C <: Controller, V <: View] = Requirements[C] => V

  trait Requirements[C <: Controller] extends Controller.Provider[C]

  trait Dependencies[C <: Controller](requirements: Requirements[C]) extends View:
    protected def controller: C = requirements.controller

  trait Provider[V <: View]:
    def view: V

trait NavigatorView extends View:
  def navigateTo[Route](route: Route): Unit = NavigableApplicationManager.navigateTo(route)
  def navigateBack(): Unit = NavigableApplicationManager.navigateBack()

abstract class BaseView[C <: Controller](requirements: View.Requirements[C])
    extends NavigatorView
    with View.Dependencies(requirements)
