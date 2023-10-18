package scatan.lib.mvc

/** The View component.
  * @tparam State
  *   The type of the state of the model.
  */
trait View[State <: Model.State]:

  /** The state of the application.
    */
  def state: State

  /** Displays the view.
    */
  def show(): Unit

  /** Hides the view.
    */
  def hide(): Unit

  /** Displays a message.
    * @param message
    *   The message to display.
    */
  def displayMessage(message: String): Unit

  /** Updates the state of the view.
    * @param state
    *   The new state of the view.
    */
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

/** A mixin trait for views that can navigate to other views.
  */
trait NavigatorView extends View[?]:
  def navigateTo[Route](route: Route): Unit = NavigableApplicationManager.navigateTo(route)
  def navigateBack(): Unit = NavigableApplicationManager.navigateBack()

/** A base class for views.
  *
  * @param requirements
  *   The requirements of the view.
  * @tparam State
  *   The type of the state of the model.
  * @tparam C
  *   The type of the controller.
  */
abstract class BaseView[State <: Model.State, C <: Controller[State]](requirements: View.Requirements[C])
    extends View[State]
    with NavigatorView
    with View.Dependencies(requirements):

  override def state: State = _state

  private var _state: State = controller.state
  override private[mvc] def updateState(state: State): Unit =
    _state = state
