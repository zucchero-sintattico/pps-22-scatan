package scatan.mvc.lib

/** The Interface for a View.
  *
  * @tparam C
  *   The type of the Controller.
  * @param requirements
  *   The Requirements for the View.
  */
trait View[C <: Controller[?]](requirements: View.Requirements[C]):
  def controller: C = requirements.controller

  def show(): Unit

  def hide(): Unit

/** The View object.
  */
object View:
  /** The Factory for a View.
    * @tparam C
    *   The type of the Controller.
    * @tparam V
    *   The type of the View.
    */
  type Factory[C <: Controller[V], V <: View[C]] = Requirements[C] => V

  /** The Requirements for a View.
    *
    * @tparam C
    *   The type of the Controller.
    */
  trait Requirements[C <: Controller[?]] extends Controller.Provider[C]

  /** The Provider for a View.
    *
    * @tparam V
    *   The type of the View.
    */
  trait Provider[V <: View[?]]:
    def view: V
