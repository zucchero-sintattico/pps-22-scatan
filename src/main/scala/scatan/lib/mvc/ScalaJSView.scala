package scatan.lib.mvc

import com.raquo.laminar.api.L.*
import org.scalajs.dom

/** A view that uses Laminar to render itself.
  * @tparam State
  *   The type of the state of the view.
  */
trait ScalaJSView[State <: Model.State](
    val container: String // The id of the container element
) extends View[State]:

  /** A signal that emits the current state of the application.
    */
  def reactiveState: Signal[State]

  /** The element that is rendered by this view.
    */
  def element: Element

  override def show(): Unit =
    val containerElement = dom.document.getElementById(container)
    containerElement.children.foreach(_.remove())
    render(containerElement, element)

  override def hide(): Unit =
    val containerElement = dom.document.getElementById(container)
    containerElement.children.foreach(_.remove())
    render(containerElement, div())

  override def displayMessage(message: String): Unit =
    dom.window.alert(message)

object ScalaJSView:
  type Factory[C <: Controller[?], V <: View[?]] = (String, View.Requirements[C]) => V

/** A base class for views that use Laminar to render themselves.
  * @param container
  *   The id of the container element
  * @param requirements
  *   The requirements of the view.
  * @tparam State
  *   The type of the state of the view.
  * @tparam C
  *   The type of the controller of the view.
  */
abstract class BaseScalaJSView[State <: Model.State, C <: Controller[State]](
    container: String,
    requirements: View.Requirements[C]
) extends BaseView[State, C](requirements)
    with ScalaJSView[State](container):

  private val _reactiveState = Var[State](controller.state)

  override def updateState(state: State): Unit =
    super.updateState(state)
    _reactiveState.writer.onNext(state)

  /** A signal that emits the current state of the application.
    */
  def reactiveState: Signal[State] = _reactiveState.signal
