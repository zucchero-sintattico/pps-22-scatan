package scatan.lib.mvc

trait View[State <: Model.State]:
  def show(): Unit
  def hide(): Unit
  def error(message: String): Unit
  private[mvc] def updateState(state: State): Unit = ()

/** The View object.
  */
object View:
  type Factory[C <: Controller[?], V <: View[?]] = Requirements[C] => V

  trait Requirements[C <: Controller[?]] extends Controller.Provider[C]

  trait Dependencies[C <: Controller[?]](requirements: Requirements[C]) extends View[?]:
    protected def controller: C = requirements.controller

  trait Provider[V <: View[?]]:
    def view: V

trait NavigatorView extends View[?]:
  def navigateTo[Route](route: Route): Unit = NavigableApplicationManager.navigateTo(route)
  def navigateBack(): Unit = NavigableApplicationManager.navigateBack()

abstract class BaseView[State <: Model.State, C <: Controller[State]](requirements: View.Requirements[C])
    extends View[State]
    with NavigatorView
    with View.Dependencies(requirements)
