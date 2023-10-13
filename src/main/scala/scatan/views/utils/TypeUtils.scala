package scatan.views.utils

import com.raquo.airstream.core.Signal
import scatan.model.ApplicationState
import scatan.model.game.ScatanState
import scatan.views.game.components.GameViewClickHandler
import scatan.views.viewmodel.{GameViewModel, ScatanViewModel}

object TypeUtils:

  type Displayable[T] = ScatanViewModel ?=> T
  type InputSource[T] = GameViewClickHandler ?=> T
  type DisplayableSource[T] = Displayable[InputSource[T]]
  type GameStateKnowledge[T] = ScatanState ?=> T
  type InputSourceWithState[T] = InputSource[GameStateKnowledge[T]]

  private[views] def reactiveState(using Signal[ApplicationState]): Signal[ApplicationState] =
    summon[Signal[ApplicationState]]
  private[views] def clickHandler(using GameViewClickHandler): GameViewClickHandler =
    summon[GameViewClickHandler]
  private[views] def scatanState(using ScatanState): ScatanState =
    summon[ScatanState]

  private[views] def applicationViewModel(using ScatanViewModel): ScatanViewModel =
    summon[ScatanViewModel]

  private[views] def gameViewModel(using scatanViewModel: ScatanViewModel): GameViewModel =
    val reactiveGame = scatanViewModel.state.map(_.game)
    GameViewModel(reactiveGame.map(_.get))

