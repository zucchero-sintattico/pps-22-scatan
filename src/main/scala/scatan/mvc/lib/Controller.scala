package scatan.mvc.lib

/** The interface for a Controller.
  *
  * @tparam V
  *   The type of the View.
  * @param requirements
  *   The requirements for the Controller.
  */
trait Controller[V <: View[?]](requirements: Controller.Requirements[V]):
  def model: Model[?] = requirements.model

  def view: V = requirements.view

/** The Controller object.
  */
object Controller:
  type Factory[V <: View[C], C <: Controller[V]] = Requirements[V] => C

  /** The requirements for a Controller.
    * @tparam V
    *   The type of the View.
    */
  trait Requirements[V <: View[?]] extends Model.Provider[?] with View.Provider[V]

  /** The provider for a Controller.
    * @tparam C
    *   The type of the Controller.
    */
  trait Provider[C <: Controller[?]]:
    def controller: C
