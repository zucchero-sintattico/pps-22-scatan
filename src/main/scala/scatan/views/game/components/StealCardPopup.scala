package scatan.views.game.components

import com.raquo.laminar.api.L.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import scatan.model.game.config.ScatanPlayer
import scatan.views.utils.TypeUtils.{DisplayableSource, gameController, reactiveState}

object StealCardPopup:

  private val toBeShown: Var[Boolean] = Var(false)

  def show(): Unit = toBeShown.writer.onNext(true)

  def userSelectionPopup(): DisplayableSource[Element] =
    val options: Signal[Seq[ScatanPlayer]] = reactiveState.map(_.game match
      case Some(game) =>
        game.playersOnRobber
          .filter(_ != game.turn.player)
      case None => Nil
    )
    div(
      display <-- toBeShown.signal.map(if _ then "block" else "none"),
      cls := "popup",
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
