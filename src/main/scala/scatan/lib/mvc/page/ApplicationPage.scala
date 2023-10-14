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
trait ApplicationPage[S <: Model.State, C <: Controller[?], V <: View[?]](
    override val model: Model[S],
    val pageFactory: PageFactory[C, V, S]
) extends View.Requirements[C]
    with Controller.Requirements[V, S]:
  override lazy val view: V = pageFactory.viewFactory(this)
  override lazy val controller: C = pageFactory.controllerFactory(this)

object ApplicationPage:

  type Factory[S <: Model.State, C <: Controller[S], V <: View[S]] =
    Model[S] => ApplicationPage[S, C, V]

  def apply[S <: Model.State, C <: Controller[?], V <: View[?]](
      model: Model[S],
      pageFactory: PageFactory[C, V, S]
  ): ApplicationPage[S, C, V] =
    new ApplicationPage[S, C, V](model, pageFactory) {}
