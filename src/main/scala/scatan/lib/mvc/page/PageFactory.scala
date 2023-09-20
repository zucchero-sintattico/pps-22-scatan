package scatan.lib.mvc.page

import scatan.lib.mvc.{Controller, Model, View}

case class PageFactory[C <: Controller, V <: View, S <: Model.State](
    viewFactory: View.Factory[C, V],
    controllerFactory: Controller.Factory[V, C, S]
)
