package scatan
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom

def appElement(): Element =
  div(
    a(
      href := "https://vitejs.dev",
      target := "_blank",
      img(src := "/vite.svg", className := "logo", alt := "Vite logo")
    ),
    a(
      href := "https://developer.mozilla.org/en-US/docs/Web/JavaScript",
      target := "_blank"
    ),
    h1("Hello Laminar!"),
    div(className := "card", button(tpe := "button")),
    p(className := "read-the-docs", "Click on the Vite logo to learn more")
  )
@main
def main(): Unit =
  val containerNode = dom.document.querySelector("#root")

  // this is how you render the rootElement in the browser
  render(containerNode, appElement())
