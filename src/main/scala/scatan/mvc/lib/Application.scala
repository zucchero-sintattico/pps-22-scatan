package scatan.mvc.lib

/** An application is a collection of pages that share a model.
  * @tparam S
  *   The state type of the model.
  * @tparam Route
  *   The type of the route.
  */
trait Application[S <: Model.State, Route](val model: Model.Interface[S]):
  val pages: Map[Route, ApplicationPage[S, ?, ?]]
