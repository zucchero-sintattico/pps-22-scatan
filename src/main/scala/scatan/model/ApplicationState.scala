package scatan.model

import scatan.lib.mvc.Model
import scatan.model.game.{Game, GameRuleDSL, Player}
import scatan.model.scatangame.{ScatanActions, ScatanPhases, ScatanRules}

final case class ApplicationState(game: Option[Game[?, ?]]) extends Model.State:
  def createGame(usernames: String*): ApplicationState =
    given GameRuleDSL[ScatanPhases, ScatanActions] = ScatanRules
    ApplicationState(Option(Game(usernames.map(Player(_)))))

object ApplicationState:
  def apply(): ApplicationState = ApplicationState(Option.empty)
