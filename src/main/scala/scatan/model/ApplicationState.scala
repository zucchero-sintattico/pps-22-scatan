package scatan.model

import scatan.lib.mvc.Model
import scatan.model.game.{Game, GameRulesDSL, Player}
import scatan.model.scatangame.{ScatanActions, ScatanPhases, ScatanRules, ScatanState}

type ScatanGame = Game[ScatanState, ScatanPhases, ScatanActions]
final case class ApplicationState(game: Option[ScatanGame]) extends Model.State:
  def createGame(usernames: String*): ApplicationState =
    given GameRulesDSL[ScatanState, ScatanPhases, ScatanActions] = ScatanRules
    ApplicationState(Option(Game(usernames.map(Player(_)))))

object ApplicationState:
  def apply(): ApplicationState = ApplicationState(Option.empty)
