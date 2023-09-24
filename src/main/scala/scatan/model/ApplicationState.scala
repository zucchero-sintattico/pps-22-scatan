package scatan.model

import scatan.lib.game.{Game, GameRulesDSL, Player}
import scatan.lib.mvc.Model
import scatan.model.game.{ScatanActions, ScatanPhases, ScatanRules, ScatanState}

type ScatanGame = Game[ScatanState, ScatanPhases, ScatanActions]
type ScatanGameDSL = GameRulesDSL[ScatanState, ScatanPhases, ScatanActions]

final case class ApplicationState(game: Option[ScatanGame]) extends Model.State:
  def createGame(usernames: String*): ApplicationState =
    given ScatanGameDSL = ScatanRules
    ApplicationState(Option(Game(usernames.map(Player(_)))))

object ApplicationState:
  def apply(): ApplicationState = ApplicationState(Option.empty)
