package scatan.views.utils

import com.raquo.airstream.core.Signal
import scatan.model.ApplicationState
import scatan.model.game.state.ScatanState
import scatan.views.game.components.GameViewClickHandler
import scatan.views.viewmodel.{GameViewModel, ScatanViewModel}

/** Utils of types that can be useful in views.
  */
object TypeUtils:

  /** A type that can be displayed in the view.
    */
  type Displayable[T] = ScatanViewModel ?=> T

  /** A type can be clicked.
    */
  type InputSource[T] = GameViewClickHandler ?=> T

  /** A type that can be displayed in the view and clicked.
    */
  type DisplayableSource[T] = Displayable[InputSource[T]]

  /** A type bring the knowledge of the game state.
    */
  type GameStateKnowledge[T] = ScatanState ?=> T

  /** A type can be clicked enriched by the knowledge of the game state.
    */
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
