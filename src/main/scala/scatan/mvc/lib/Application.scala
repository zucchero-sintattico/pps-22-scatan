package scatan.mvc.lib

/** An application is a collection of pages that share a model.
  * @tparam S
  *   The state type of the model.
  * @tparam Route
  *   The type of the route.
  */
trait Application[S <: Model.State, Route]:
  val model: Model.Interface[S]
  val pages: Map[Route, ApplicationPage[S, ?, ?]]
object Application:
  /** Create an application from a model and a list of pages.
    * @param model
    *   The model.
    * @param pagesFactories
    *   The pages.
    * @tparam S
    *   The state type of the model.
    * @tparam Route
    *   The type of the route.
    * @return
    *   The application.
    */
  def apply[S <: Model.State, Route](
      initialState: S,
      pagesFactories: Map[Route, PageFactory[?, ?]]
  ): Application[S, Route] =
    new Application[S, Route]:
      override val model: Model.Interface[S] = Model(initialState)
      override val pages: Map[Route, ApplicationPage[S, ?, ?]] =
        pagesFactories.map((k, v) => (k, ApplicationPage(model, v)))
