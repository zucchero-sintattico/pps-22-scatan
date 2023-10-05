package scatan.model.game

import scatan.lib.game.Game
import scatan.model.components.{DevelopmentCard, DevelopmentType}
import scatan.model.game.ScatanState
import scatan.model.game.ops.CardOps.{assignDevelopmentCard, consumeDevelopmentCard}

class StateWithDevCardsTest extends BasicStateTest:

  "A State with development cards" should "have empty development cards when game start" in {
    val state = ScatanState(threePlayers)
    state.developmentCards(threePlayers.head) should be(Seq.empty[DevelopmentCard])
  }

  it should "allow to assign development cards" in {
    val initialGame = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val stateWithDevCardsAssigned = initialGame
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    initialGame.developmentCards(player1) should be(Seq.empty[DevelopmentCard])
    stateWithDevCardsAssigned.developmentCards(player1) should be(
      Seq(DevelopmentCard(DevelopmentType.Knight), DevelopmentCard(DevelopmentType.Knight))
    )
  }

  it should "allow to consume development cards" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val state2 = state.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    state2.developmentCards(player1) should be(Seq(DevelopmentCard(DevelopmentType.Knight)))
    state2.developmentCards(player2) should be(Seq.empty[DevelopmentCard])
    val state3 = state2.consumeDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    state3.developmentCards(player1) should be(Seq.empty[DevelopmentCard])
    state3.developmentCards(player2) should be(Seq.empty[DevelopmentCard])
    val state4 = state3.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    state4.developmentCards(player1) should be(Seq(DevelopmentCard(DevelopmentType.Knight)))
    state4.developmentCards(player2) should be(Seq.empty[DevelopmentCard])
    val state5 = state4.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    state5.developmentCards(player1) should be(
      Seq(DevelopmentCard(DevelopmentType.Knight), DevelopmentCard(DevelopmentType.Knight))
    )
    state5.developmentCards(player2) should be(Seq.empty[DevelopmentCard])
    val state6 = state5.consumeDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    state6.developmentCards(player1) should be(Seq(DevelopmentCard(DevelopmentType.Knight)))
    state6.developmentCards(player2) should be(Seq.empty[DevelopmentCard])
    val state7 = state6.consumeDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    state7.developmentCards(player1) should be(Seq.empty[DevelopmentCard])
    state7.developmentCards(player2) should be(Seq.empty[DevelopmentCard])
  }

  it should "not allow to consume development cards if the player does not have it" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val state2 = state.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    state2.developmentCards(player1) should be(Seq(DevelopmentCard(DevelopmentType.Knight)))
    state2.developmentCards(player2) should be(Seq.empty[DevelopmentCard])
    val state3 = state2.consumeDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
    state3.developmentCards(player1) should be(Seq(DevelopmentCard(DevelopmentType.Knight)))
    state3.developmentCards(player2) should be(Seq.empty[DevelopmentCard])
  }
