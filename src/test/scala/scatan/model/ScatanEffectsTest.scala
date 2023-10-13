package scatan.model

import scatan.BaseTest
import scatan.model.components.{ResourceCard, ResourceType}
import scatan.model.game.ScatanEffects.{NextTurnEffect, PlaceRobberEffect, RollEffect, TradeWithPlayerEffect}
import scatan.model.game.ScatanGame
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.state.ScatanState
import scatan.model.game.state.ops.CardOps.assignResourceCard

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

  "A RollDice Effect" should "assign resources to players" in {
    val state = ScatanState(players)
    val effect = RollEffect(6)
    val stateAfterRoll = effect(state)
    stateAfterRoll should not be None
  }

  "A MoveRobber Effect" should "move the robber in valid position" in {
    val state = ScatanState(players)
    val effect =
      PlaceRobberEffect(
        state.gameMap.tileWithTerrain.find(h => h != state.robberPlacement).getOrElse(state.gameMap.tiles.head)
      )
    val stateAfterRoll = effect(state)
    stateAfterRoll should not be None
  }

  it should "not move the robber in invalid position" in {
    val state = ScatanState(players)
    val effect = PlaceRobberEffect(state.robberPlacement)
    val stateAfterRoll = effect(state)
    stateAfterRoll should be(None)
  }

  "A Next Turn Effect" should "exists" in {
    val state = ScatanState(players)
    val effect = NextTurnEffect()
    val stateAfterRoll = effect(state)
    stateAfterRoll should not be None
  }
