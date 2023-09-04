package scatan.mvc.lib

class Model[S <: Model.State](val state: S)

/** The Model object. It encapsulate the base model traits and the apply method to create a State-based model.
  */
object Model:
  trait State
  trait Provider[S <: State]:
    def model: Model[S]
  def apply[S <: State](state: S): Model[S] = new Model(state)
