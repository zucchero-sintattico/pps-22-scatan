package scatan.mvc

trait State

package mvc:
  object Model:
    class Interface[S <: State](val state: S)
    trait Provider[S <: State]:
      def model: Interface[S]
    def apply[S <: State](state: S): Interface[S] = Interface(state)

  object View:
    trait Requirements[C <: Controller.Interface[?]] extends Controller.Provider[C]
    trait Interface[C <: Controller.Interface[?]](requirements: Requirements[C]):
      def controller: C = requirements.controller
      def show(): Unit
      def hide(): Unit
    trait Provider[V <: Interface[?]]:
      def view: V
    def apply[V <: Interface[?]](_view: V): Provider[V] = new Provider[V]:
      override def view: V = _view

  object Controller:
    trait Requirements[V <: View.Interface[?]] extends Model.Provider[?] with View.Provider[V]
    trait Interface[V <: View.Interface[?]](requirements: Requirements[V]):
      def model: Model.Interface[?] = requirements.model
      def view: V = requirements.view
    trait Provider[C <: Interface[?]]:
      def controller: C
    def apply[C <: Interface[?]](_controller: C): Provider[C] = new Provider[C]:
      override def controller: C = _controller

  trait ApplicationPage[S <: State, C <: Controller.Interface[?], V <: View.Interface[?]](
      val model: Model.Interface[S],
      viewFactory: View.Requirements[C] => V,
      controllerFactory: Controller.Requirements[V] => C
  ) extends Model.Provider[S]
      with View.Requirements[C]
      with Controller.Requirements[V]:
    override def view: V = viewFactory(this)
    override def controller: C = controllerFactory(this)

  trait Application[S <: State, Route](val model: Model.Interface[S]):
    val pages: Map[Route, ApplicationPage[S, ?, ?]]

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

//
// Example
//

import mvc.*
case class MyState(value: Int) extends State

object HomePage:

  def apply(model: Model.Interface[MyState]): ApplicationPage[MyState, HomeController, HomeView] =
    new ApplicationPage[MyState, HomeController, HomeView](model, new HomeView(_), new HomeController(_)) {}

  class HomeView(requirements: View.Requirements[HomeController]) extends View.Interface[HomeController](requirements):
    override def show(): Unit =
      println("HomeView.show")
      renderHome()
    override def hide(): Unit =
      println("HomeView.hide")
    def renderHome(): Unit =
      println("HomeView.renderHome")
      controller.controlHome()

  class HomeController(requirements: Controller.Requirements[HomeView])
      extends Controller.Interface[HomeView](requirements):
    def controlHome(): Unit =
      println("HomeController.controlHome")

object AboutPage:

  def apply(model: Model.Interface[MyState]): ApplicationPage[MyState, AboutController, AboutView] =
    new ApplicationPage[MyState, AboutController, AboutView](model, new AboutView(_), new AboutController(_)) {}

  class AboutView(requirements: View.Requirements[AboutController])
      extends View.Interface[AboutController](requirements):
    override def show(): Unit =
      println("AboutView.show")
      renderAbout()
    override def hide(): Unit =
      println("AboutView.hide")
    def renderAbout(): Unit =
      println("AboutView.renderAbout")
      controller.controlAbout()

  class AboutController(requirements: Controller.Requirements[AboutView])
      extends Controller.Interface[AboutView](requirements):
    def controlAbout(): Unit =
      println("AboutController.controlAbout")

enum Page:
  case Home, About
object MyApplication extends Application[MyState, Page](Model(MyState(1))) with MementoApplication[Page]:
  override val pages: Map[Page, ApplicationPage[MyState, ?, ?]] = Map(
    Page.Home -> HomePage(this.model),
    Page.About -> AboutPage(this.model)
  )

@main def run(): Unit =
  val app = MyApplication
  app.show(Page.Home)
  app.show(Page.About)
  app.back()
