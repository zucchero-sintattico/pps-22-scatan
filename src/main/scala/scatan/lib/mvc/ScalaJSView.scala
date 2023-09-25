package scatan.lib.mvc

import com.raquo.laminar.api.L.*
import org.scalajs.dom

trait ScalaJSView(val container: String) extends View:
  def element: Element

  override def show(): Unit =
    val containerElement = dom.document.getElementById(container)
    containerElement.children.foreach(_.remove())
    render(containerElement, element)

  override def hide(): Unit =
    val containerElement = dom.document.getElementById(container)
    containerElement.children.foreach(_.remove())
    render(containerElement, div())

object ScalaJSView:
  type Factory[C <: Controller, V <: View] = (String, View.Requirements[C]) => V

abstract class BaseScalaJSView[C <: Controller](container: String, requirements: View.Requirements[C])
    extends BaseView(requirements)
    with ScalaJSView(container)
