package scatan.mvc.lib

/** The Model object. It encapsulate the base model traits and the apply method to create a State-based model.
  */
object Model:
  trait State
  class Interface[S <: State](val state: S)
  trait Provider[S <: State]:
    def model: Interface[S]
  def apply[S <: State](state: S): Interface[S] = Interface(state)
