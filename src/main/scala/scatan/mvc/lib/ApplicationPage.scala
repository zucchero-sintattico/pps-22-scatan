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
  * @param viewFactory
  *   A factory for the view.
  * @param controllerFactory
  *   A factory for the controller.
  */
trait ApplicationPage[S <: Model.State, C <: Controller.Interface[V], V <: View.Interface[C]](
    val model: Model.Interface[S],
    viewFactory: View.Factory[C, V],
    controllerFactory: Controller.Factory[V, C]
) extends Model.Provider[S]
    with View.Requirements[C]
    with Controller.Requirements[V]:
  override def view: V = viewFactory(this)
  override def controller: C = controllerFactory(this)

object ApplicationPage:
  type Factory[S <: Model.State, C <: Controller.Interface[V], V <: View.Interface[C]] =
    Model.Interface[S] => ApplicationPage[S, ?, ?]
  def apply[S <: Model.State, C <: Controller.Interface[V], V <: View.Interface[C]](
      model: Model.Interface[S],
      viewFactory: View.Factory[C, V],
      controllerFactory: Controller.Factory[V, C]
  ): ApplicationPage[S, C, V] =
    new ApplicationPage[S, C, V](model, viewFactory, controllerFactory) {}
