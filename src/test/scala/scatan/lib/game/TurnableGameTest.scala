package scatan.lib.game

import scatan.BaseTest

class TurnableGameTest extends BaseTest:

  val players = Seq(Player("p1"), Player("p2"))

  "A Turnable Game" should "be able to be created" in {
    val game = TurnableGame(
      players,
      0,
      Turn(1, players(0))
    )
  }

  it should "have a turn" in {
    val game = TurnableGame(
      players,
      0,
      Turn(1, players(0))
    )
    game.turn should be(Turn(1, players(0)))
  }

  it should "be nextable" in {
    val game = TurnableGame(
      players,
      0,
      Turn(1, players(0))
    )
    game.nextTurn should be(
      TurnableGame(
        players,
        0,
        Turn(2, players(1))
      )
    )
  }

  it should "not be able to set a turn with a non present player" in {
    assertThrows[IllegalArgumentException] {
      val game = TurnableGame(
        players,
        0,
        Turn(1, Player("p3"))
      )
    }
  }
