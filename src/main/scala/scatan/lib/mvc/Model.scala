package scatan.lib.mvc

trait Model[S <: Model.State](private var _state: S):
  def state: S = _state
  def update(f: S => S): Unit = _state = f(_state)

object Model:
  trait State
  def apply[S <: State](state: S): Model[S] = new Model(state) {}

  trait Provider[S <: State]:
    def model: Model[S]
