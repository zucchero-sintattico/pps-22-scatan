package scatan.views.viewmodel.ops

import com.raquo.airstream.core.Signal
import scatan.model.game.config.ScatanPlayer
import scatan.views.viewmodel.GameViewModel

object ViewModelWinOps:

  extension (gameViewModel: GameViewModel)

    def winner: Signal[Option[ScatanPlayer]] =
      gameViewModel.state.map(_.winner)

    def winnerName: Signal[String] =
      winner.map(_.map(_.name).getOrElse("No one"))

    def isEnded: Signal[Boolean] =
      winner.map(_.isDefined)
