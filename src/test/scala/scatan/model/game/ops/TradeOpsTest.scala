package scatan.model.game.ops

import scatan.model.game.BaseScatanStateTest
import scatan.model.game.ScatanState
import scatan.model.game.ops.CardOps.assignResourceCard
import scatan.model.components.ResourceCard
import scatan.model.components.ResourceType
import scatan.model.game.ops.TradeOps.tradeWithPlayer
import scatan.model.game.ops.TradeOps.tradeWithBank

class TradeOpsTest extends BaseScatanStateTest:

  "A State with trade Ops" should "allow to trade resources" in {
    val state = ScatanState(threePlayers)
    val sender = threePlayers.head
    val receiver = threePlayers.tail.head
    val stateWithResourceAssigned = state
      .assignResourceCard(sender, ResourceCard(ResourceType.Wood))
      .flatMap(_.assignResourceCard(sender, ResourceCard(ResourceType.Wood)))
      .flatMap(_.assignResourceCard(receiver, ResourceCard(ResourceType.Brick)))
    stateWithResourceAssigned match
      case Some(state) =>
        val stateWithTrade =
          state.tradeWithPlayer(sender, receiver, state.resourceCards(sender), state.resourceCards(receiver))
        stateWithTrade match
          case Some(state) =>
            state.resourceCards(sender) should be(Seq(ResourceCard(ResourceType.Brick)))
            state.resourceCards(receiver) should be(
              Seq(ResourceCard(ResourceType.Wood), ResourceCard(ResourceType.Wood))
            )
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

  it should "remove only the cards that are involved in the trade from a player " in {
    val state = ScatanState(threePlayers)
    val sender = threePlayers.head
    val receiver = threePlayers.tail.head
    val stateWithResourceAssigned = state
      .assignResourceCard(sender, ResourceCard(ResourceType.Wood))
      .flatMap(_.assignResourceCard(sender, ResourceCard(ResourceType.Wood)))
      .flatMap(_.assignResourceCard(sender, ResourceCard(ResourceType.Wood)))
      .flatMap(_.assignResourceCard(receiver, ResourceCard(ResourceType.Brick)))
    stateWithResourceAssigned match
      case Some(state) =>
        val stateWithTrade =
          state.tradeWithPlayer(sender, receiver, Seq(ResourceCard(ResourceType.Wood)), state.resourceCards(receiver))
        stateWithTrade match
          case Some(state) =>
            state.resourceCards(sender) should be(
              Seq(ResourceCard(ResourceType.Wood), ResourceCard(ResourceType.Wood), ResourceCard(ResourceType.Brick))
            )
            state.resourceCards(receiver) should be(Seq(ResourceCard(ResourceType.Wood)))
          case None => fail("Trade not allowed")
      case None => fail("Resources not assigned")
  }

  it should "allow to trade a card with bank for four identical cards" in {
    val state = ScatanState(threePlayers)
    val player = threePlayers.head
    val stateWithResourceAssigned = state
      .assignResourceCard(player, ResourceCard(ResourceType.Wood))
      .flatMap(_.assignResourceCard(player, ResourceCard(ResourceType.Wood)))
      .flatMap(_.assignResourceCard(player, ResourceCard(ResourceType.Wood)))
      .flatMap(_.assignResourceCard(player, ResourceCard(ResourceType.Wood)))
    stateWithResourceAssigned match
      case Some(state) =>
        val stateWithTrade =
          state.tradeWithBank(
            player,
            Seq(
              ResourceCard(ResourceType.Wood),
              ResourceCard(ResourceType.Wood),
              ResourceCard(ResourceType.Wood),
              ResourceCard(ResourceType.Wood)
            ),
            ResourceCard(ResourceType.Brick)
          )
        stateWithTrade match
          case Some(state) =>
            state.resourceCards(player) should contain(ResourceCard(ResourceType.Brick))
          case None => fail("Trade not allowed")
      case None => fail("Resources not assigned")
  }

  it should "not allow to trade with bank if the player hasn't the cards" in {
    val state = ScatanState(threePlayers)
    val player = threePlayers.head
    val stateWithResourceAssigned = state
      .assignResourceCard(player, ResourceCard(ResourceType.Wood))
      .flatMap(_.assignResourceCard(player, ResourceCard(ResourceType.Wood)))
      .flatMap(_.assignResourceCard(player, ResourceCard(ResourceType.Wood)))
    stateWithResourceAssigned match
      case Some(state) =>
        val stateWithTrade =
          state.tradeWithBank(
            player,
            Seq(
              ResourceCard(ResourceType.Wood),
              ResourceCard(ResourceType.Wood),
              ResourceCard(ResourceType.Wood),
              ResourceCard(ResourceType.Wood)
            ),
            ResourceCard(ResourceType.Brick)
          )
        stateWithTrade should be(None)
      case None => fail("Resources not assigned")
  }

  it should "not allow to trade with bank if the offer not contains four identical cards" in {
    val state = ScatanState(threePlayers)
    val player = threePlayers.head
    val stateWithResourceAssigned = state
      .assignResourceCard(player, ResourceCard(ResourceType.Wood))
      .flatMap(_.assignResourceCard(player, ResourceCard(ResourceType.Wood)))
      .flatMap(_.assignResourceCard(player, ResourceCard(ResourceType.Wood)))
      .flatMap(_.assignResourceCard(player, ResourceCard(ResourceType.Wood)))
      .flatMap(_.assignResourceCard(player, ResourceCard(ResourceType.Brick)))
    stateWithResourceAssigned match
      case Some(state) =>
        val stateWithTrade =
          state.tradeWithBank(
            player,
            Seq(
              ResourceCard(ResourceType.Wood),
              ResourceCard(ResourceType.Wood),
              ResourceCard(ResourceType.Wood),
              ResourceCard(ResourceType.Brick)
            ),
            ResourceCard(ResourceType.Brick)
          )
        stateWithTrade should be(None)
      case None => fail("Resources not assigned")
  }
