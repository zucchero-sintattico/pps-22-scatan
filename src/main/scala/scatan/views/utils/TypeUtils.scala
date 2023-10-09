package scatan.views.utils

import scatan.model.ApplicationState
import scatan.controllers.game.GameController
import scatan.model.game.ScatanState
import com.raquo.airstream.core.Signal

object TypeUtils:

  type Displayable[T] = Signal[ApplicationState] ?=> T
  type InputSource[T] = GameController ?=> T
  type DisplayableSource[T] = Displayable[InputSource[T]]
  type StateKnoledge[T] = ScatanState ?=> T
  type InputSourceWithState[T] = InputSource[StateKnoledge[T]]
