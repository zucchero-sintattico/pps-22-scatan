package scatan.model.game.ops

import scatan.model.game.BaseScatanStateTest
import scatan.model.game.ScatanState
import scatan.model.game.ops.CardOps.assignResourceCard
import scatan.model.components.ResourceCard
import scatan.model.components.ResourceType
import scatan.model.game.ops.TradeOps.tradeWithPlayer

class TradeOpsTest extends BaseScatanStateTest:

  "A State with trade Ops" should "allow to trade resources" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val stateWithResourceAssigned = state
      .assignResourceCard(player1, ResourceCard(ResourceType.Wood))
      .flatMap(
        _.assignResourceCard(player2, ResourceCard(ResourceType.Brick))
      )
    stateWithResourceAssigned match
      case Some(state) =>
        state.resourceCards(player1) should be(Seq(ResourceCard(ResourceType.Wood)))
        state.resourceCards(player2) should be(Seq(ResourceCard(ResourceType.Brick)))
        val stateWithTrade =
          state.tradeWithPlayer(player1, player2, state.resourceCards(player1), state.resourceCards(player2))
        stateWithTrade match
          case Some(state) =>
            state.resourceCards(player1) should be(Seq(ResourceCard(ResourceType.Brick)))
            state.resourceCards(player2) should be(Seq(ResourceCard(ResourceType.Wood)))
          case None => fail("Trade not allowed")
      case None => fail("Resources not assigned")
  }

  it should "not allow to trade resources if the player does not have it" in {
    val state = ScatanState(threePlayers)
    val sender = threePlayers.head
    val receiver = threePlayers.tail.head
    val stateWithResourceAssigned = state
      .assignResourceCard(sender, ResourceCard(ResourceType.Wood))
      .flatMap(
        _.assignResourceCard(receiver, ResourceCard(ResourceType.Brick))
      )
    stateWithResourceAssigned match
      case Some(state) =>
        state.resourceCards(sender) should be(Seq(ResourceCard(ResourceType.Wood)))
        state.resourceCards(receiver) should be(Seq(ResourceCard(ResourceType.Brick)))
        val stateWithTrade =
          state.tradeWithPlayer(sender, receiver, Seq(ResourceCard(ResourceType.Brick)), state.resourceCards(receiver))
        stateWithTrade should be(None)
      case None => fail("Resources not assigned")
  }
