package scatan.mvc.lib

/** The View object.
  */
object View:
  /** The Factory for a View.
    * @tparam C
    *   The type of the Controller.
    * @tparam V
    *   The type of the View.
    */
  type Factory[C <: Controller.Interface[V], V <: Interface[C]] = Requirements[C] => V

  /** The Requirements for a View.
    *
    * @tparam C
    *   The type of the Controller.
    */
  trait Requirements[C <: Controller.Interface[?]] extends Controller.Provider[C]

  /** The Interface for a View.
    *
    * @tparam C
    *   The type of the Controller.
    * @param requirements
    *   The Requirements for the View.
    */
  trait Interface[C <: Controller.Interface[?]](requirements: Requirements[C]):
    def controller: C = requirements.controller
    def show(): Unit
    def hide(): Unit

  /** The Provider for a View.
    *
    * @tparam V
    *   The type of the View.
    */
  trait Provider[V <: Interface[?]]:
    def view: V
