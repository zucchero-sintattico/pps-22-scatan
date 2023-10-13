package scatan.views.viewmodel

import com.raquo.airstream.core.Signal
import scatan.model.ApplicationState
import scatan.model.components.{Awards, DevelopmentType, ResourceType}
import scatan.model.game.config.{ScatanActions, ScatanPhases, ScatanPlayer, ScatanSteps}
import scatan.model.game.ops.ScoreOps.scores
import scatan.model.game.{ScatanGame, ScatanState}
import scatan.views.game.components.CardContextMap.CardType

trait ViewModel[S]:
  def state: Signal[S]

class ScatanViewModel(override val state: Signal[ApplicationState]) extends ViewModel[ApplicationState]

class GameViewModel(override val state: Signal[ScatanGame]) extends ViewModel[ScatanGame]:

  def currentPhase: Signal[ScatanPhases] = state.map(_.gameStatus.phase)
  def currentStep: Signal[ScatanSteps] = state.map(_.gameStatus.step)
  def currentAwards: Signal[Awards] = state.map(_.state.assignedAwards)
  def gameState: Signal[ScatanState] = state.map(_.state)

