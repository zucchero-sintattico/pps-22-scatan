package scatan.model

import scatan.lib.mvc.Model
import scatan.model.game.ScatanGame
import scatan.model.game.config.ScatanPlayer
import map.GameMap

final case class ApplicationState(game: Option[ScatanGame]) extends Model.State:
  def createGame(gameMap: GameMap, usernames: String*): ApplicationState =
    val players = usernames.map(ScatanPlayer(_))
    ApplicationState(Option(ScatanGame(gameMap, players)))

object ApplicationState:
  def apply(): ApplicationState = ApplicationState(Option.empty)
