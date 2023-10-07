package scatan.model

import scatan.BaseTest
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.ScatanState
import scatan.model.components.ResourceCard
import scatan.model.components.ResourceType
import scatan.model.game.ScatanEffects.TradeWithPlayerEffect
import scatan.model.game.ops.CardOps.assignResourceCard
import scatan.model.game.ScatanGame

class ScatanEffectsTest extends BaseTest:

  val players = Seq(ScatanPlayer("Player 1"), ScatanPlayer("Player 2"), ScatanPlayer("Player 3"))
  val state = ScatanState(players)

  "A TradeWithPlayerEffect" should "be a valid effect if both players have the cards" in {
    val resourceCard = ResourceCard(ResourceType.Brick)
    val tradeSender = state.players.head
    val tradeReceiver = state.players.tail.head
    val stateWithResourceCard = for
      stateWithSenderCard <- state.assignResourceCard(tradeSender, ResourceCard(ResourceType.Brick))
      stateWithReceiverCard <- stateWithSenderCard.assignResourceCard(tradeReceiver, ResourceCard(ResourceType.Sheep))
    yield stateWithReceiverCard
    stateWithResourceCard match
      case Some(state) =>
        val effect = TradeWithPlayerEffect(
          tradeSender,
          tradeReceiver,
          state.resourceCards(tradeSender),
          state.resourceCards(tradeReceiver)
        )
        val stateAfterTrade = effect(state)
        stateAfterTrade match
          case Some(s) =>
            s.resourceCards(tradeSender) should be(Seq(ResourceCard(ResourceType.Sheep)))
            s.resourceCards(tradeReceiver) should be(Seq(ResourceCard(ResourceType.Brick)))
          case None => fail("Could not complete the trade")

      case None => fail("Could not assign resource cards")
  }
