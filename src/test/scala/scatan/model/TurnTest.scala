package scatan.model

import scatan.BaseTest

class TurnTest extends BaseTest:

  "A turn" should "have a number" in {
    val turn = Turn(1, Player("a"))
    turn.number shouldBe 1
  }

  it should "not allow to have a number less than 1" in {
    assertThrows[IllegalArgumentException] {
      Turn(0, Player("a"))
    }
  }

  it should "have a player" in {
    val turn = Turn(1, Player("a"))
    turn.currentPlayer shouldBe Player("a")
  }

  it should "not allow to have a player with an empty name" in {
    assertThrows[IllegalArgumentException] {
      Turn(1, Player(""))
    }
  }

  it should "have a phase" in {
    val turn = Turn(1, Player("a"))
    turn.currentPhase shouldBe Phase.Initial
  }
