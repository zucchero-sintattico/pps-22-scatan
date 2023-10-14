package scatan.lib.mvc

/** A model is a container for a state object. It provides a way to update the state object.
  * @tparam S
  *   The type of the state object.
  */
trait Model[S <: Model.State]:
  def state: S
  def update(f: S => S): Unit

object Model:
  trait State
  def apply[S <: State](initialState: S): Model[S] =
    new Model[S]:
      private var _state = initialState
      override def state: S = _state
      override def update(f: S => S): Unit = _state = f(_state)

  trait Provider[S <: State]:
    def model: Model[S]
