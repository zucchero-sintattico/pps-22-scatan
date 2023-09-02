package scatan.mvc.lib

trait MementoApplication[Route] extends Application[?, Route]:
  private var routes: Seq[Route] = Seq.empty
  def show(route: Route): Unit =
    routes.lastOption.foreach(pages(_).view.hide())
    routes = routes :+ route
    pages(route).view.show()
  def back(): Unit =
    routes.lastOption.foreach(pages(_).view.hide())
    routes = routes.dropRight(1)
    pages(routes.last).view.show()
