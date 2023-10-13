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

  def currentPlayer: Signal[ScatanPlayer] = state.map(_.turn.player)
  def currentPlayerScore: Signal[Int] = state.map(game => game.state.scores(game.turn.player))
  def currentPhase: Signal[ScatanPhases] = state.map(_.gameStatus.phase)
  def currentStep: Signal[ScatanSteps] = state.map(_.gameStatus.step)
  def allowedActions: Signal[Seq[ScatanActions]] = state.map(_.allowedActions.toSeq)
  def isActionDisabled(action: ScatanActions): Signal[Boolean] = allowedActions.map(!_.contains(action))
  def currentAwards: Signal[Awards] = state.map(_.state.assignedAwards)
  def gameState: Signal[ScatanState] = state.map(_.state)


  def cardCountOfCurrentPlayer(cardType: CardType): Signal[Int] =
    for
      game <- state
      player = game.turn.player
    yield game.state.countCardOf(player)(cardType)

  extension (state: ScatanState)
    private def countCardOf(player: ScatanPlayer)(cardType: CardType): Int = cardType match
      case resourceType: ResourceType =>
        state.resourceCards(player).count(_.resourceType == resourceType)
      case developmentType: DevelopmentType =>
        state.developmentCards(player).count(_.developmentType == developmentType)
