package scatan.mvc.lib

import org.scalajs.dom
import com.raquo.laminar.api.L.*

trait ScalaJS(val container: String) extends View:
  def element: Element

  override def show(): Unit =
    val containerElement = dom.document.getElementById(container)
    render(containerElement, element)

  override def hide(): Unit =
    val containerElement = dom.document.getElementById(container)
    render(containerElement, div())
