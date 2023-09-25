package scatan
import com.raquo.laminar.api.L.given
import scatan.lib.mvc.NavigableApplicationManager
import scatan.lib.mvc.application.NavigableApplication
import scatan.model.ApplicationState

val Application = NavigableApplication(
  initialState = ApplicationState(),
  pagesFactories = Pages.values.map(p => p -> p.pageFactory).toMap
)

@main def main(): Unit =
  NavigableApplicationManager.startApplication(Application, Pages.Home)
