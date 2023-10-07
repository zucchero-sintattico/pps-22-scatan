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

  val players = Seq(ScatanPlayer("Player 1"), ScatanPlayer("Player 2"))
  val state = ScatanState(players)

  "A TradeWithPlayerEffect" should "be a valid effect if both players have the cards" in {
    val resourceCard = ResourceCard(ResourceType.Brick)
    val tradeSender = state.players.head
    val tradeReceiver = state.players.last
    val stateWithResourceCard = for
      stateWithSenderCard <- state.assignResourceCard(tradeSender, ResourceCard(ResourceType.Brick))
      stateWithReceiverCard <- stateWithSenderCard.assignResourceCard(tradeReceiver, ResourceCard(ResourceType.Sheep))
    yield stateWithReceiverCard
    val effect = TradeWithPlayerEffect(
      tradeSender,
      tradeReceiver,
      state.resourceCards(tradeSender),
      state.resourceCards(tradeReceiver)
    )
    stateWithResourceCard match
      case Some(state) => effect(state) should be(Some(state))
      case None        => fail("Could not assign resource cards")
  }
