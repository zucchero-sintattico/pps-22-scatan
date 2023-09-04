package scatan.mvc.lib.page

import scatan.mvc.lib.{Controller, Model, View}

case class PageFactory[C <: Controller[V, S], V <: View[C], S <: Model.State](
    viewFactory: View.Factory[C, V],
    controllerFactory: Controller.Factory[V, C, S]
)
