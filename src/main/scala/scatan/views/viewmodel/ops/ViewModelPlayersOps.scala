package scatan.views.viewmodel.ops

import com.raquo.airstream.core.Signal
import scatan.model.components.{DevelopmentType, ResourceType}
import scatan.model.game.ScatanState
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.ops.ScoreOps.scores
import scatan.views.game.components.CardContextMap.CardType
import scatan.views.viewmodel.GameViewModel

object ViewModelPlayersOps:

  extension (gameViewModel: GameViewModel)

    def currentPlayer: Signal[ScatanPlayer] =
      gameViewModel.state.map(_.turn.player)

    def currentPlayerScore: Signal[Int] =
      gameViewModel.state.map(game => game.state.scores(game.turn.player))

    def playersOnRobberExceptCurrent: Signal[Seq[ScatanPlayer]] =
      gameViewModel.state.map(game => game.playersOnRobber.filter(_ != game.turn.player))

    def cardCountOfCurrentPlayer(cardType: CardType): Signal[Int] =
      for
        game <- gameViewModel.state
        player = game.turn.player
      yield game.state.countCardOf(player)(cardType)

  extension (state: ScatanState)
    private def countCardOf(player: ScatanPlayer)(cardType: CardType): Int = cardType match
      case resourceType: ResourceType =>
        state.resourceCards(player).count(_.resourceType == resourceType)
      case developmentType: DevelopmentType =>
        state.developmentCards(player).count(_.developmentType == developmentType)
