package scatan.model

import scatan.BaseTest
import scatan.model.game.{Player, Turn}

class TurnTest extends BaseTest:

  val player = Player("a")

  "A turn" should "have a number" in {
    val turn = Turn(1, player)
    turn.number shouldBe 1
  }

  it should "not allow to have a number less than 1" in {
    assertThrows[IllegalArgumentException] {
      Turn(0, player)
    }
  }

  it should "have a player" in {
    val turn = Turn(1, player)
    turn.player shouldBe player
  }

  it should "be nextable passing next player" in {
    val turn = Turn(1, player)
    val nextTurn = turn.next(Player("b"))
    nextTurn.number shouldBe 2
    nextTurn.player shouldBe Player("b")
  }
