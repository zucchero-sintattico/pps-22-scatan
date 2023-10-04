package scatan.model

import scatan.lib.game.dsl.TypedGameDSL
import scatan.lib.game.{Game, Rules}
import scatan.lib.mvc.Model
import scatan.model.game.{ScatanActions, ScatanDSL, ScatanPhases, ScatanPlayer, ScatanState, ScatanSteps}

type ScatanGame = Game[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer]
type ScatanRules = Rules[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer]

final case class ApplicationState(game: Option[ScatanGame]) extends Model.State:
  def createGame(usernames: String*): ApplicationState =
    given ScatanRules = ScatanDSL.rules
    ApplicationState(Option(Game(usernames.map(ScatanPlayer(_)))))

object ApplicationState:
  def apply(): ApplicationState = ApplicationState(Option.empty)
