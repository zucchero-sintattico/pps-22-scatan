package scatan.views.game.components

import com.raquo.airstream.core.Signal
import scatan.model.ApplicationState
import scatan.controllers.game.GameController
import com.raquo.laminar.api.L.*
import scatan.model.game.ScatanGame
import scatan.model.game.ScatanState
import scatan.model.game.ops.AwardOps.awards
import scatan.model.components.Award
import scatan.model.components.AwardType

object AwardsComponent:

  def awardsComponent(using reactiveState: Signal[ApplicationState])(using gameController: GameController): Element =
    div(
      className := "awards",
      h2("Current awards"),
      child <-- reactiveState
        .map(state =>
          (for
            game <- state.game
            gameState = game.state
          yield getCurrentAwards(gameState))
            .getOrElse(div("No game"))
        )
    )
  private def getCurrentAwards(state: ScatanState): Element =
    div(
      ul(
        li(
          "Longest road: ",
          state
            .awards(Award(AwardType.LongestRoad))
            .map(award => s"${award._1.name} (${award._2} roads)")
            .getOrElse("Nobody yet")
        ),
        li(
          "Largest army: ",
          state
            .awards(Award(AwardType.LargestArmy))
            .map(award => s"${award._1.name} (${award._2} cards)")
            .getOrElse("Nobody yet")
        )
      )
    )
