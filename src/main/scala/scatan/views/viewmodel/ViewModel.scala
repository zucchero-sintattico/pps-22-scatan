package scatan.views.viewmodel

import com.raquo.airstream.core.Signal
import scatan.model.ApplicationState
import scatan.model.game.ScatanGame

trait ViewModel[S]:
  def state: Signal[S]

class ScatanViewModel(override val state: Signal[ApplicationState]) extends ViewModel[ApplicationState]

class GameViewModel(override val state: Signal[ScatanGame]) extends ViewModel[ScatanGame]
