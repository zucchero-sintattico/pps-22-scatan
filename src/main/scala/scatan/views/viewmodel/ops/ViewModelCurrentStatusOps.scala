package scatan.views.viewmodel.ops

import com.raquo.airstream.core.Signal
import scatan.model.components.Awards
import scatan.model.game.config.{ScatanPhases, ScatanSteps}
import scatan.model.game.state.ScatanState
import scatan.views.viewmodel.GameViewModel

object ViewModelCurrentStatusOps:
  extension (gameViewModel: GameViewModel)

    def currentPhase: Signal[ScatanPhases] =
      gameViewModel.state.map(_.gameStatus.phase)

    def currentStep: Signal[ScatanSteps] =
      gameViewModel.state.map(_.gameStatus.step)

    def currentAwards: Signal[Awards] =
      gameViewModel.state.map(_.state.assignedAwards)

    def currentState: Signal[ScatanState] =
      gameViewModel.state.map(_.state)
