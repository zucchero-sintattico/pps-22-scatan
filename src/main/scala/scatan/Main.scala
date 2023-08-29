package scatan
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

def appElement(): Element =
  div(
    h1("Hello Laminar!"),
    div(
      className := "card",
      button(
        className := "button",
        "Click me!",
        tpe := "button"
      )
    ),
    p(className := "read-the-docs", "Click on the Vite logo to learn more")
  )
@main
def main(): Unit =
  val containerNode = dom.document.querySelector("#root")

  // this is how you render the rootElement in the browser
  render(containerNode, appElement())
