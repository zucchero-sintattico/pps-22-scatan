package scatan.views.game.components

import com.raquo.laminar.api.L.*
import org.scalajs.dom
import scatan.views.utils.TypeUtils.{DisplayableSource, gameViewModel, reactiveState}
import scatan.views.viewmodel.ops.ViewModelWinOps.{isEnded, winner, winnerName}

object EndgameComponent:

  /**
   * A popup that appears when the game is over
   * @return the component
   */
  def endgamePopup: DisplayableSource[Element] =
    div(
      display <-- gameViewModel.isEnded
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
      child.text <-- gameViewModel.winnerName,
      br(),
      button(
        "Restart",
        onClick --> { _ =>
          // Close the popup, you can implement your own logic here
          dom.window.location.reload()
        }
      )
    )
