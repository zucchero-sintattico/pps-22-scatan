package scatan.mvc.lib

case class PageFactory[C <: Controller.Interface[V], V <: View.Interface[C]](
    viewFactory: View.Factory[C, V],
    controllerFactory: Controller.Factory[V, C]
)
