package scatan.mvc.lib

trait Model[S <: Model.State](var state: S)

object Model:
  trait State
  trait Provider[S <: State]:
    def model: Model[S]
  def apply[S <: State](state: S): Model[S] = new Model(state) {}
