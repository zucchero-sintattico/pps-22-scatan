package scatan.lib.mvc

import org.scalajs.dom
import com.raquo.laminar.api.L.*
import scatan.lib.mvc.View

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
