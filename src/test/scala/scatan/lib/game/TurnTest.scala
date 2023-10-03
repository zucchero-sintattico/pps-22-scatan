package scatan.lib.game

import scatan.BaseTest
import scatan.model.game.ScatanPlayer

class TurnTest extends BaseTest:

  val player = ScatanPlayer("a")

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
    val nextTurn = turn.next(ScatanPlayer("b"))
    nextTurn.number shouldBe 2
    nextTurn.player shouldBe ScatanPlayer("b")
  }
