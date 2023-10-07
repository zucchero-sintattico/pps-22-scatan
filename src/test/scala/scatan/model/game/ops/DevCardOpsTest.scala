package scatan.model.game.ops

import scatan.lib.game.Game
import scatan.model.components.{DevelopmentCard, DevelopmentType}
import scatan.model.game.ScatanState
import scatan.model.game.ops.CardOps.{assignDevelopmentCard, consumeDevelopmentCard}
import scatan.model.game.BaseScatanStateTest

class DevCardOpsTest extends BaseScatanStateTest:

  "A State with development cards Ops" should "have empty development cards when game start" in {
    val state = ScatanState(threePlayers)
    state.developmentCards(threePlayers.head) should be(Seq.empty[DevelopmentCard])
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
