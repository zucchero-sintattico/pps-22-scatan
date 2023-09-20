package scatan.model

import scatan.lib.mvc.Model
import scatan.lib.mvc.Model

final case class ApplicationState(game: Option[Game]) extends Model.State:
  def createGame(usernames: String*): ApplicationState =
    ApplicationState(Option(Game(usernames.map(Player(_)))))
