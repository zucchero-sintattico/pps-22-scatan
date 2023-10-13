package scatan.model.game

import scatan.lib.mvc.Model
import scatan.model.ApplicationState

object ScatanModelOps:

  extension (model: Model[ApplicationState])
    /** Update the game state if there is a game in progress.
      * @param update
      *   A function that takes the current game state and returns the next game state.
      * @return
      *   The next game state if there is a game in progress, otherwise None.
      */
    def updateGame(update: ScatanGame => Option[ScatanGame]): Option[ScatanGame] =
      for
        game <- model.state.game
        nextGame <- update(game)
      yield
        model.update(_.copy(game = Some(nextGame)))
        nextGame

  extension (game: Option[ScatanGame])
    /** Run the callback if there is no game.
      * @param callback
      *   The callback to run.
      */
    def onError(callback: => Unit): Unit = game match
      case None    => callback
      case Some(_) => ()
