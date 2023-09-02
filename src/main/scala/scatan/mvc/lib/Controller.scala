package scatan.mvc.lib

/** The Controller object.
  */
object Controller:
  type Factory[V <: View.Interface[C], C <: Controller.Interface[V]] = Controller.Requirements[V] => C

  /** The requirements for a Controller.
    * @tparam V
    *   The type of the View.
    */
  trait Requirements[V <: View.Interface[?]] extends Model.Provider[?] with View.Provider[V]

  /** The interface for a Controller.
    * @tparam V
    *   The type of the View.
    * @param requirements
    *   The requirements for the Controller.
    */
  trait Interface[V <: View.Interface[?]](requirements: Requirements[V]):
    def model: Model.Interface[?] = requirements.model

    def view: V = requirements.view

  /** The provider for a Controller.
    * @tparam C
    *   The type of the Controller.
    */
  trait Provider[C <: Interface[?]]:
    def controller: C
