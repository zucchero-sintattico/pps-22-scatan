package scatan.model.game.ops

import scatan.model.components.*
import scatan.model.game.ops.CardOps.assignResourceCard
import scatan.model.game.ops.TradeOps.{tradeWithBank, tradeWithPlayer}
import scatan.model.game.{BaseScatanStateTest, ScatanState}

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
      case Some(stateWithResourceAssigned) =>
        val stateWithTrade =
          stateWithResourceAssigned.tradeWithPlayer(
            sender,
            receiver,
            Seq(ResourceCard(ResourceType.Wood), ResourceCard(ResourceType.Wood)),
            Seq(ResourceCard(ResourceType.Brick))
          )
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
      .flatMap(_.assignResourceCard(sender, ResourceCard(ResourceType.Brick)))
      .flatMap(_.assignResourceCard(receiver, ResourceCard(ResourceType.Wheat)))
      .flatMap(_.assignResourceCard(receiver, ResourceCard(ResourceType.Rock)))
    stateWithResourceAssigned match
      case Some(state) =>
        val stateWithTrade =
          state.tradeWithPlayer(
            sender,
            receiver,
            Seq(ResourceCard(ResourceType.Wood)),
            Seq(ResourceCard(ResourceType.Rock))
          )
        stateWithTrade match
          case Some(state) =>
            state.resourceCards(sender) should contain(ResourceCard(ResourceType.Brick))
            state.resourceCards(sender) should contain(ResourceCard(ResourceType.Rock))
            state.resourceCards(sender) should have size 2
            state.resourceCards(receiver) should contain(ResourceCard(ResourceType.Wheat))
            state.resourceCards(receiver) should contain(ResourceCard(ResourceType.Wood))
            state.resourceCards(receiver) should have size 2

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
            ResourceType.Wood,
            ResourceType.Brick
          )
        stateWithTrade match
          case Some(state) =>
            state.resourceCards(player) should contain(ResourceCard(ResourceType.Brick))
            state.resourceCards(player) should have size 1
          case None => fail("Trade not allowed")
      case None => fail("Resources not assigned")
  }

  it should "remove only traded cards from player after trade with bank" in {
    val state = ScatanState(threePlayers)
    val player = threePlayers.head
    val stateWithResourceAssigned = state
      .assignResourceCard(player, ResourceCard(ResourceType.Wood))
      .flatMap(_.assignResourceCard(player, ResourceCard(ResourceType.Wood)))
      .flatMap(_.assignResourceCard(player, ResourceCard(ResourceType.Wood)))
      .flatMap(_.assignResourceCard(player, ResourceCard(ResourceType.Wood)))
      .flatMap(_.assignResourceCard(player, ResourceCard(ResourceType.Wood)))
      .flatMap(_.assignResourceCard(player, ResourceCard(ResourceType.Rock)))
    stateWithResourceAssigned match
      case Some(state) =>
        val stateWithTrade =
          state.tradeWithBank(
            player,
            ResourceType.Wood,
            ResourceType.Brick
          )
        stateWithTrade match
          case Some(state) =>
            state.resourceCards(player) should contain(ResourceCard(ResourceType.Brick))
            state.resourceCards(player) should contain(ResourceCard(ResourceType.Rock))
            state.resourceCards(player) should contain(ResourceCard(ResourceType.Wood))
            state.resourceCards(player) should have size 3
          case None => fail("Trade not allowed")
      case None => fail("Resources not assigned")
  }

  it should "not allow to trade with bank if player doesn't have four identical cards" in {
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
            ResourceType.Wood,
            ResourceType.Brick
          )
        stateWithTrade should be(None)
      case None => fail("Resources not assigned")
  }
