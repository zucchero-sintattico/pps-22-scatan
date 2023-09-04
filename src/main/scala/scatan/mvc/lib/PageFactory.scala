package scatan.mvc.lib

case class PageFactory[C <: Controller[V], V <: View[C]](
    viewFactory: View.Factory[C, V],
    controllerFactory: Controller.Factory[V, C]
)
