package scatan.lib.mvc.application

import scatan.lib.mvc.Model
import scatan.lib.mvc.page.{ApplicationPage, PageFactory}

/** An application is a collection of pages that share a model.
  *
  * @tparam S
  *   The state type of the model.
  * @tparam Route
  *   The type of the route.
  */
trait Application[S <: Model.State, Route]:
  val model: Model[S]
  val pages: Map[Route, ApplicationPage[S, ?, ?]]

object Application:
  /** Create an application from a model and a list of pages.
    * @param initialState
    *   The initial state of the model.
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
      pagesFactories: Map[Route, PageFactory[?, ?, S]]
  ): Application[S, Route] =
    new Application[S, Route]:
      override val model: Model[S] = Model(initialState)
      override val pages: Map[Route, ApplicationPage[S, ?, ?]] =
        pagesFactories.map((route, pageFactory) => (route, ApplicationPage(model, pageFactory)))
