package scatan.lib.mvc

import com.raquo.laminar.api.L.*
import org.scalajs.dom

trait ScalaJSView[State <: Model.State](
    val container: String,
    val initialState: State
) extends View[State]:

  private val _reactiveState = Var[State](initialState)
  val reactiveState: Signal[State] = _reactiveState.signal
  override def updateState(state: State): Unit =
    _reactiveState.writer.onNext(state)

  def element: Element

  override def show(): Unit =
    val containerElement = dom.document.getElementById(container)
    containerElement.children.foreach(_.remove())
    render(containerElement, element)

  override def hide(): Unit =
    val containerElement = dom.document.getElementById(container)
    containerElement.children.foreach(_.remove())
    render(containerElement, div())

  override def error(message: String): Unit =
    dom.window.alert(message)

object ScalaJSView:
  type Factory[C <: Controller[?], V <: View[?]] = (String, View.Requirements[C]) => V

abstract class BaseScalaJSView[State <: Model.State, C <: Controller[State]](
    container: String,
    requirements: View.Requirements[C]
) extends BaseView[State, C](requirements)
    with ScalaJSView[State](
      container,
      requirements.controller.state
    )
