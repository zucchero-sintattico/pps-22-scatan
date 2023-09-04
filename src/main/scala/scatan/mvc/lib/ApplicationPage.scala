package scatan.mvc.lib

/** A page of an application. It is a combination of a model, a view and a controller.
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
trait ApplicationPage[S <: Model.State, C <: Controller[V], V <: View[C]](
    val model: Model[S],
    val pageFactory: PageFactory[C, V]
) extends Model.Provider[S]
    with View.Requirements[C]
    with Controller.Requirements[V]:
  override def view: V = pageFactory.viewFactory(this)
  override def controller: C = pageFactory.controllerFactory(this)

object ApplicationPage:
  type Factory[S <: Model.State, C <: Controller[V], V <: View[C]] =
    Model[S] => ApplicationPage[S, ?, ?]
  def apply[S <: Model.State, C <: Controller[V], V <: View[C]](
      model: Model[S],
      pageFactory: PageFactory[C, V]
  ): ApplicationPage[S, C, V] =
    new ApplicationPage[S, C, V](model, pageFactory) {}
