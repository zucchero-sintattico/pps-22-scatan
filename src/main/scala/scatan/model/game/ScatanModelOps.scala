package scatan.model.game

import scatan.lib.mvc.Model
import scatan.model.ApplicationState

object ScatanModelOps:

  extension (model: Model[ApplicationState])
    def updateGame(update: ScatanGame => Option[ScatanGame]): Option[ScatanGame] =
      for
        game <- model.state.game
        nextGame <- update(game)
      yield
        model.update(_.copy(game = Some(nextGame)))
        nextGame

  extension (game: Option[ScatanGame])
    def onError(callback: => Unit): Unit = game match
      case None => callback
