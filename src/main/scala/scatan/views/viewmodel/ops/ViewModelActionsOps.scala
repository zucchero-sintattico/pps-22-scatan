package scatan.views.viewmodel.ops

import com.raquo.airstream.core.Signal
import scatan.model.game.config.ScatanActions
import scatan.views.viewmodel.GameViewModel

object ViewModelActionsOps:

  extension (gameViewModel: GameViewModel)

    def allowedActions: Signal[Seq[ScatanActions]] =
      gameViewModel.state.map(_.allowedActions.toSeq)

    def isActionEnabled(action: ScatanActions): Signal[Boolean] =
      allowedActions.map(_.contains(action))

    def isActionDisabled(action: ScatanActions): Signal[Boolean] =
      allowedActions.map(!_.contains(action))

    def canBuyDevelopment: Signal[Boolean] =
      allowedActions.map(_.contains(ScatanActions.BuyDevelopmentCard))

