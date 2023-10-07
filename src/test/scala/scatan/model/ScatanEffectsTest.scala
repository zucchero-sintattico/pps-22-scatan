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
    val sender = state.players.head
    val receiver = state.players.tail.head
    val stateWithResourceCard = state
      .assignResourceCard(sender, ResourceCard(ResourceType.Brick))
      .flatMap(_.assignResourceCard(receiver, ResourceCard(ResourceType.Sheep)))
    stateWithResourceCard match
      case Some(state) =>
        val effect = TradeWithPlayerEffect(
          sender,
          receiver,
          state.resourceCards(sender),
          state.resourceCards(receiver)
        )
        val stateAfterTrade = effect(state)
        stateAfterTrade should not be None
      case None => fail("Could not assign resource cards")
  }

  it should "return None if the trade is not allowed" in {
    val resourceCard = ResourceCard(ResourceType.Brick)
    val sender = state.players.head
    val receiver = state.players.tail.head
    val stateWithResourceCard = state
      .assignResourceCard(sender, ResourceCard(ResourceType.Brick))
      .flatMap(_.assignResourceCard(receiver, ResourceCard(ResourceType.Sheep)))
    stateWithResourceCard match
      case Some(state) =>
        val effect = TradeWithPlayerEffect(
          sender,
          receiver,
          Seq(ResourceCard(ResourceType.Sheep)),
          state.resourceCards(receiver)
        )
        val stateAfterTrade = effect(state)
        stateAfterTrade should be(None)
      case None => fail("Could not assign resource cards")
  }
