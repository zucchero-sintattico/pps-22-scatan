package scatan.model.game.ops

import scatan.lib.game.Game
import scatan.model.components.{DevelopmentCard, DevelopmentType, ResourceCard, ResourceType}
import scatan.model.game.{BaseScatanStateTest, ScatanState}
import scatan.model.game.ops.DevCardOps.{buyDevelopmentCard, consumeDevelopmentCard, assignDevelopmentCard}
import scatan.model.game.ops.ResCardOps.assignResourceCard

class DevCardOpsTest extends BaseScatanStateTest:

  "A State with development cards Ops" should "have empty development cards when game start" in {
    val state = ScatanState(threePlayers)
    state.developmentCards(threePlayers.head) should be(Seq.empty[DevelopmentCard])
  }

  it should "not allow to buy a development card without enough resources" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    state.buyDevelopmentCard(player1, 1) should be(None)
  }

  it should "allow to buy a development card with enough resources" in {
    val initialState = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val stateWithEnoughResources = for
      stateWithOneResource <- initialState.assignResourceCard(player1, ResourceCard(ResourceType.Wheat))
      stateWithTwoResource <- stateWithOneResource.assignResourceCard(player1, ResourceCard(ResourceType.Sheep))
      stateWithThreeResource <- stateWithTwoResource.assignResourceCard(player1, ResourceCard(ResourceType.Rock))
    yield stateWithThreeResource
    stateWithEnoughResources match
      case Some(state) =>
        state.developmentCards(player1) should be(Seq.empty[DevelopmentCard])
        val stateWithDevCardBought = state.buyDevelopmentCard(player1, 1)
        stateWithDevCardBought match
          case Some(state) =>
            state.developmentCards(player1).size should be(1)
          case None => fail("stateWithDevCardBought should be defined")
      case None => fail("stateWithEnoughResources should be defined")
  }

  it should "allow to assign development cards" in {
    val initialGame = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val stateWithDevCardsAssigned = for
      stateWithOneDevCard <- initialGame.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      stateWithTwoDevCard <- stateWithOneDevCard.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    yield stateWithTwoDevCard
    initialGame.developmentCards(player1) should be(Seq.empty[DevelopmentCard])
    stateWithDevCardsAssigned match
      case Some(state) =>
        state.developmentCards(player1) should be(
          Seq(DevelopmentCard(DevelopmentType.Knight), DevelopmentCard(DevelopmentType.Knight))
        )
        state.developmentCards(player2) should be(Seq.empty[DevelopmentCard])
      case None => fail("stateWithDevCardsAssigned should be defined")
  }

  it should "allow to consume development cards" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val stateWithDevCardAssigned = state.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    stateWithDevCardAssigned match
      case Some(state) =>
        state.developmentCards(player1) should be(Seq(DevelopmentCard(DevelopmentType.Knight)))
        state.developmentCards(player2) should be(Seq.empty[DevelopmentCard])
        val stateWithDevCardConsumed = state.consumeDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
        stateWithDevCardConsumed match
          case Some(state) =>
            state.developmentCards(player1) should be(Seq.empty[DevelopmentCard])
            state.developmentCards(player2) should be(Seq.empty[DevelopmentCard])
          case None => fail("stateWithDevCardConsumed should be defined")
      case None => fail("stateWithDevCardAssigned should be defined")
  }

  it should "not allow to consume development cards if the player does not have it" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val stateWithDevCardAssigned = state.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    stateWithDevCardAssigned match
      case Some(state) =>
        state.developmentCards(player1) should be(Seq(DevelopmentCard(DevelopmentType.Knight)))
        val stateWithDevCardConsumed = state.consumeDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
        stateWithDevCardConsumed match
          case Some(state) =>
            state.developmentCards(player1) should be(Seq.empty[DevelopmentCard])
            state.developmentCards(player2) should be(Seq.empty[DevelopmentCard])
          case None => fail("stateWithDevCardConsumed should be defined")
      case None => fail("stateWithDevCardAssigned should be defined")
  }
