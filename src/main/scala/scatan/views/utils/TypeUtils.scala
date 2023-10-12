package scatan.views.utils

import com.raquo.airstream.core.Signal
import scatan.model.ApplicationState
import scatan.model.game.ScatanState
import scatan.views.game.components.GameViewClickHandler

object TypeUtils:

  type Displayable[T] = Signal[ApplicationState] ?=> T
  type InputSource[T] = GameViewClickHandler ?=> T
  type DisplayableSource[T] = Displayable[InputSource[T]]
  type StateKnoledge[T] = ScatanState ?=> T
  type InputSourceWithState[T] = InputSource[StateKnoledge[T]]

  private[views] def reactiveState(using Signal[ApplicationState]): Signal[ApplicationState] =
    summon[Signal[ApplicationState]]
  private[views] def clickHandler(using GameViewClickHandler): GameViewClickHandler =
    summon[GameViewClickHandler]
  private[views] def state(using ScatanState): ScatanState =
    summon[ScatanState]
