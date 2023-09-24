package scatan.lib.mvc.page

import scatan.lib.mvc.{Controller, Model, View}

/** A page of an application. It is a combination of a model, a view and a controller.
  *
  * @tparam S
  *   The type of the state of the model.
  * @tparam C
  *   The type of the controller.
  * @tparam V
  *   The type of the view.
  * @param model
  *   The model.
  * @param pageFactory
  *   The page factory.
  */
trait ApplicationPage[S <: Model.State, C <: Controller, V <: View](
    override val model: Model[S],
    val pageFactory: PageFactory[C, V, S]
) extends View.Requirements[C]
    with Controller.Requirements[V, S]:
  private lazy val _controller: C = pageFactory.controllerFactory(this)
  private lazy val _view: V = pageFactory.viewFactory(this)
  override def controller: C = _controller
  override def view: V = _view

object ApplicationPage:
  type Factory[S <: Model.State, C <: Controller, V <: View] =
    Model[S] => ApplicationPage[S, C, V]
  def apply[S <: Model.State, C <: Controller, V <: View](
      model: Model[S],
      pageFactory: PageFactory[C, V, S]
  ): ApplicationPage[S, C, V] =
    new ApplicationPage[S, C, V](model, pageFactory) {}
