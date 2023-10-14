package scatan.views.game.components

import com.raquo.laminar.api.L.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import scatan.model.game.config.ScatanPlayer
import scatan.views.utils.TypeUtils.{DisplayableSource, clickHandler, gameViewModel}
import scatan.views.viewmodel.ops.ViewModelPlayersOps.playersOnRobberExceptCurrent

object StealCardPopup:

  private val toBeShown: Var[Boolean] = Var(false)

  def show(): Unit = toBeShown.writer.onNext(true)

  /** Displays a popup for selecting the user to steal a card from.
    * @return
    *   the element.
    */
  def userSelectionPopup(): DisplayableSource[Element] =
    val options: Signal[Seq[ScatanPlayer]] = gameViewModel.playersOnRobberExceptCurrent
    div(
      display <-- toBeShown.signal.map(if _ then "block" else "none"),
      cls := "popup",
      children <-- options.split(_.name) { case (_, player: ScatanPlayer, _) =>
        button(
          player.name,
          onClick --> { _ =>
            // Close the popup, you can implement your own logic here
            clickHandler.onStealCardClick(player)
            toBeShown.writer.onNext(false)
          }
        )
      }
    )
