package scatan.lib.mvc.page

import scatan.lib.mvc.{Controller, Model, ScalaJSView, View}

/** A factory for creating pages.
  * @tparam C
  *   The controller type.
  * @tparam V
  *   The view type.
  * @tparam S
  *   The state type.
  */
trait PageFactory[C <: Controller[?], V <: View[?], S <: Model.State]:
  def viewFactory: View.Factory[C, V]
  def controllerFactory: Controller.Factory[V, C, S]

object PageFactory:
  def apply[C <: Controller[S], V <: View[S], S <: Model.State](
      _viewFactory: View.Factory[C, V],
      _controllerFactory: Controller.Factory[V, C, S]
  ): PageFactory[C, V, S] =
    new PageFactory[C, V, S]:
      override val viewFactory: View.Factory[C, V] = _viewFactory
      override val controllerFactory: Controller.Factory[V, C, S] = _controllerFactory

object ScalaJSPageFactory:
  def apply[C <: Controller[?], V <: View[?], S <: Model.State](
      scalaJSViewFactory: ScalaJSView.Factory[C, V],
      _controllerFactory: Controller.Factory[V, C, S]
  )(using root: String): PageFactory[C, V, S] =
    new PageFactory[C, V, S]:
      override val viewFactory: View.Factory[C, V] = scalaJSViewFactory(root, _)
      override val controllerFactory: Controller.Factory[V, C, S] = _controllerFactory
