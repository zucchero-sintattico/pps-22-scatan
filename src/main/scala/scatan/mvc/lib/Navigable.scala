package scatan.mvc.lib

trait Navigable[Route] extends Application[?, Route]:
  private var pagesHistory: Seq[Route] = Seq.empty
  def show(route: Route): Unit =
    pagesHistory.lastOption.foreach(this.pages(_).view.hide())
    pagesHistory = pagesHistory :+ route
    pages(route).view.show()
  def back(): Unit =
    pagesHistory.lastOption.foreach(pages(_).view.hide())
    pagesHistory = pagesHistory.dropRight(1)
    pagesHistory.lastOption.foreach(pages(_).view.show())

object NavigableApplication:
  def apply[S <: Model.State, Route](
      initialState: S,
      pagesFactories: Map[Route, PageFactory[?, ?]]
  ): Application[S, Route] with Navigable[Route] =
    new Application[S, Route] with Navigable[Route]:
      override val model: Model[S] = Model(initialState)
      override val pages: Map[Route, ApplicationPage[S, ?, ?]] = pagesFactories.map { (route, pageFactory) =>
        route -> ApplicationPage(this.model, pageFactory)
      }
