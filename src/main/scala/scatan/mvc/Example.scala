package scatan.mvc

trait State

object ModelModule:
  trait Model[S <: State]:
    def state: S
  trait Provider[S <: State]:
    def model: Model[S]

object ViewModule:
  trait Requirements[C <: ControllerModule.Controller[?]] extends ControllerModule.Provider[C]
  trait View[C <: ControllerModule.Controller[?]](requirements: Requirements[C]):
    def controller: C = requirements.controller
    def render(): Unit
  trait Provider[V <: View[?]]:
    def view: V

object ControllerModule:
  trait Requirements[V <: ViewModule.View[?]] extends ModelModule.Provider[?] with ViewModule.Provider[V]
  trait Controller[V <: ViewModule.View[?]](requirements: Requirements[V]):
    def model: ModelModule.Model[?] = requirements.model
    def view: V = requirements.view
  trait Provider[C <: Controller[?]]:
    def controller: C

trait MVCProvider[S <: State, C <: ControllerModule.Controller[?], V <: ViewModule.View[?]]

class ModelImpl[S <: State](val state: S) extends ModelModule.Model[S]
case class MyState(value: Int) extends State

trait Application[S <: State, C <: ControllerModule.Controller[?], V <: ViewModule.View[?]](
    val state: S,
    viewBuilder: ViewModule.Requirements[C] => V,
    controllerBuilder: ControllerModule.Requirements[V] => C
) extends ModelModule.Provider[S]
    with ViewModule.Requirements[C]
    with ControllerModule.Requirements[V]:
  override def model: ModelModule.Model[S] = new ModelImpl(state)
  override def view: V = viewBuilder(this)
  override def controller: C = controllerBuilder(this)

  def render(): Unit =
    view.render()

class HomeView(requirements: ViewModule.Requirements[HomeController])
    extends ViewModule.View[HomeController](requirements):
  override def render(): Unit =
    println("HomeView.render")
    controller.home()

class HomeController(requirements: ControllerModule.Requirements[HomeView])
    extends ControllerModule.Controller[HomeView](requirements):
  def home(): Unit =
    println("HomeController.home")
    println(model.state)

class HomeApplication(state: MyState)
    extends Application[MyState, HomeController, HomeView](
      state,
      new HomeView(_),
      new HomeController(_)
    )

class GameView(requirements: ViewModule.Requirements[GameController])
    extends ViewModule.View[GameController](requirements):
  override def render(): Unit =
    println("GameView.render")
    controller.game()

class GameController(requirements: ControllerModule.Requirements[GameView])
    extends ControllerModule.Controller[GameView](requirements):
  def game(): Unit =
    println("GameController.game")
    println(model.state)

class GameApplication(state: MyState)
    extends Application[MyState, GameController, GameView](
      state,
      new GameView(_),
      new GameController(_)
    )

@main def run(): Unit =
  val state: MyState = MyState(1)
  var app: Application[?, ?, ?] = HomeApplication(state)
  app.render()
  app = GameApplication(state.copy(value = 2))
  app.render()
