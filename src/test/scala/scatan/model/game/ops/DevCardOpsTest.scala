package scatan.model.game.ops

import scatan.lib.game.Game
import scatan.model.components.{DevelopmentCard, DevelopmentType}
import scatan.model.game.ScatanState
import scatan.model.game.ops.CardOps.{assignDevelopmentCard, consumeDevelopmentCard}
import scatan.model.game.BasicStateTest

class DevCardOpsTest extends BasicStateTest:

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
    val state2 = state.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    state2 match
      case Some(state) =>
        state.developmentCards(player1) should be(Seq(DevelopmentCard(DevelopmentType.Knight)))
        state.developmentCards(player2) should be(Seq.empty[DevelopmentCard])
        val state3 = state.consumeDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
        state3 match
          case Some(state) =>
            state.developmentCards(player1) should be(Seq.empty[DevelopmentCard])
            state.developmentCards(player2) should be(Seq.empty[DevelopmentCard])
          case None => fail("state3 should be defined")
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
