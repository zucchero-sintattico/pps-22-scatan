package scatan.lib.mvc.page

import scatan.lib.mvc.{Controller, Model, View}
import scatan.lib.mvc.ScalaJSView

trait PageFactory[C <: Controller, V <: View, S <: Model.State]:
  def viewFactory: View.Factory[C, V]
  def controllerFactory: Controller.Factory[V, C, S]

object PageFactory:
  def apply[C <: Controller, V <: View, S <: Model.State](
      viewFactory: View.Factory[C, V],
      controllerFactory: Controller.Factory[V, C, S]
  ): PageFactory[C, V, S] =
    new PageFactory[C, V, S]:
      override def viewFactory: View.Factory[C, V] = viewFactory
      override def controllerFactory: Controller.Factory[V, C, S] = controllerFactory

object ScalaJSPageFactory:
  def apply[C <: Controller, V <: View, S <: Model.State](
      scalaJSviewFactory: ScalaJSView.Factory[C, V],
      controllerFactory: Controller.Factory[V, C, S]
  )(using root: String): PageFactory[C, V, S] =
    new PageFactory[C, V, S]:
      override def viewFactory: View.Factory[C, V] = scalaJSviewFactory(root, _)
      override def controllerFactory: Controller.Factory[V, C, S] = controllerFactory
