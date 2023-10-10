package scatan.views.game.components

import com.raquo.laminar.api.L.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import scatan.model.game.config.ScatanPlayer
import scatan.views.utils.TypeUtils.{DisplayableSource, gameController, reactiveState}

object StealCardPopup:

  private val toBeShown: Var[Boolean] = Var(false)

  def show(): Unit = toBeShown.writer.onNext(true)

  def userSelectionPopup(): DisplayableSource[ReactiveHtmlElement.Base] =
    val options: Signal[Seq[ScatanPlayer]] = reactiveState.map(_.game match
      case Some(game) =>
        game.players
          .filter(_ != game.turn.player)
      case None => Nil
    )
    div(
      display <-- toBeShown.signal.map(if _ then "block" else "none"),
      position.fixed,
      top("50%"),
      left("50%"),
      transform := "translate(-50%, -50%)",
      padding := "20px",
      backgroundColor := "white",
      boxShadow := "0 2px 10px rgba(0, 0, 0, 0.1)",
      borderRadius := "5px",
      zIndex := 1000,
      children <-- options.split(_.name) { case (_, player: ScatanPlayer, _) =>
        button(
          player.name,
          onClick --> { _ =>
            // Close the popup, you can implement your own logic here
            gameController.stealCard(player)
            toBeShown.writer.onNext(false)
          }
        )
      }
    )
