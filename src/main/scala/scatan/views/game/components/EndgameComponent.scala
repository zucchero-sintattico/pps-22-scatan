package scatan.views.game.components

import com.raquo.laminar.api.L.*
import org.scalajs.dom
import scatan.views.utils.TypeUtils.{DisplayableSource, reactiveState}

object EndgameComponent:
  def endgamePopup: DisplayableSource[Element] =
    val winnerSignal: Signal[Option[String]] = reactiveState.map(_.game.flatMap(_.winner.map(_.name)))
    div(
      display <-- winnerSignal
        .map(_.isDefined)
        .map(if _ then "block" else "none"), // Show or hide based on the presence of a winner
      position.fixed,
      top("50%"),
      left("50%"),
      transform := "translate(-50%, -50%)",
      padding := "20px",
      backgroundColor := "white",
      boxShadow := "0 2px 10px rgba(0, 0, 0, 0.1)",
      borderRadius := "5px",
      zIndex := 1000,
      child.text <-- winnerSignal.map(_.getOrElse("No winner")),
      br(),
      button(
        "Restart",
        onClick --> { _ =>
          // Close the popup, you can implement your own logic here
          dom.window.location.reload()
        }
      )
    )
