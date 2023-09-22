package scatan.model

import scatan.lib.mvc.Model
import scatan.lib.mvc.Model
import game.{Game, Player}

final case class ApplicationState(game: Option[Game[?]]) extends Model.State:
  def createGame(usernames: String*): ApplicationState =
    ApplicationState(Option(Game(usernames.map(Player(_)))))

object ApplicationState:
  def apply(): ApplicationState = ApplicationState(Option.empty)
