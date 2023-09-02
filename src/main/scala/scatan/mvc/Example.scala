package scatan.mvc

import scatan.mvc.lib.*

case class MyState(value: Int) extends Model.State

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

class AboutView(requirements: View.Requirements[AboutController]) extends View.Interface[AboutController](requirements):
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
    Page.Home -> ApplicationPage(
      model = this.model,
      viewFactory = (requirements: View.Requirements[HomeController]) => new HomeView(requirements),
      controllerFactory = (requirements: Controller.Requirements[HomeView]) => new HomeController(requirements)
    ),
    Page.About -> ApplicationPage(
      model = this.model,
      viewFactory = (requirements: View.Requirements[AboutController]) => new AboutView(requirements),
      controllerFactory = (requirements: Controller.Requirements[AboutView]) => new AboutController(requirements)
    )
  )

@main def run(): Unit =
  val app = MyApplication
  app.show(Page.Home)
  app.show(Page.About)
  app.back()
