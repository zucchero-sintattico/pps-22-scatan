package scatan.mvc.lib

import scatan.mvc.lib

trait Controller

/** The Controller object.
  */
object Controller:
  type Factory[V <: View, C <: Controller, S <: Model.State] = Requirements[V, S] => C
  trait Requirements[V <: View, S <: Model.State] extends Model.Provider[S] with View.Provider[V]

  trait Dependencies[V <: View, S <: Model.State](requirements: Requirements[V, S]) extends Controller:
    protected def view: V = requirements.view
    protected def model: Model[S] = requirements.model

  trait Provider[C <: Controller]:
    def controller: C
