package scatan.lib.game

import scatan.BaseTest

class StateGameTest extends BaseTest:

  val players = Seq(Player("Player 1"), Player("Player 2"))

  "A State Game" should "be able to be created" in {
    val stateGame = StateGame(
      players = players,
      state = 0
    )
    stateGame.players should be(players)
    stateGame.state should be(0)
  }

  it should "have players" in {
    val stateGame = StateGame(
      players = players,
      state = 0
    )
    stateGame.players should be(players)
  }

  it should "have a state" in {
    val stateGame = StateGame(
      players = players,
      state = 0
    )
    stateGame.state should be(0)
  }

  it should "accept any type of state" in {
    val stateGame = StateGame(
      players = players,
      state = "Hello"
    )
    stateGame.state should be("Hello")
  }

  it should "not be able to be created with no players" in {
    assertThrows[IllegalArgumentException] {
      StateGame(
        players = Seq.empty,
        state = 0
      )
    }
  }

  it should "not be able to be created with no state" in {
    assertThrows[IllegalArgumentException] {
      StateGame(
        players = players,
        state = null
      )
    }
  }
