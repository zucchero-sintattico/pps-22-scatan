package scatan.lib.mvc

trait Controller[State <: Model.State]:
  def state: State

/** The Controller object.
  */
object Controller:
  type Factory[V <: View[?], C <: Controller[?], S <: Model.State] = Requirements[V, S] => C
  trait Requirements[V <: View[?], S <: Model.State] extends Model.Provider[S] with View.Provider[V]

  trait Dependencies[V <: View[S], S <: Model.State](requirements: Requirements[V, S]) extends Controller[S]:
    protected def view: V = requirements.view
    protected def model: Model[S] = requirements.model

  trait Provider[C <: Controller[?]]:
    def controller: C

/** A wrapper for a model that updates the view when the model is updated.
  * @param view
  *   The view to update
  * @param model
  *   The model to wrap
  * @tparam S
  *   The state type
  */
class ReactiveModelWrapper[S <: Model.State](view: => View[S], model: Model[S]) extends Model[S]:
  private val internalModel = model
  override def state: S = internalModel.state
  override def update(f: S => S): Unit =
    internalModel.update(f)
    view.updateState(this.state)

/** A base controller that wraps a model and a view.
  * @param requirements
  *   The requirements for the controller
  * @tparam V
  *   The view type
  * @tparam S
  *   The state type
  */
abstract class BaseController[V <: View[S], S <: Model.State](requirements: Controller.Requirements[V, S])
    extends Controller[S]
    with Controller.Dependencies(requirements):

  override def state: S = model.state
  override protected val model: Model[S] =
    new ReactiveModelWrapper(requirements.view, requirements.model)

/** A controller that does nothing.
  * @param requirements
  *   The requirements for the controller
  * @tparam State
  *   The state type
  */
class EmptyController[State <: Model.State, V <: View[State]](requirements: Controller.Requirements[V, State])
    extends BaseController(requirements)
