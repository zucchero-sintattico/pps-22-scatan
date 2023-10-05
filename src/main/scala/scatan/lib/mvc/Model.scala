package scatan.lib.mvc

trait Model[S <: Model.State]:
  def state: S
  def update(f: S => S): Unit

object Model:
  trait State
  def apply[S <: State](__state: S): Model[S] =
    new Model[S]:
      private var _state = __state
      override def state: S = _state
      override def update(f: S => S): Unit = _state = f(_state)

  trait Provider[S <: State]:
    def model: Model[S]
