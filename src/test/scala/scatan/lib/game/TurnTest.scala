package scatan.lib.game

import scatan.BaseTest
import scatan.model.game.config.ScatanPlayer

class TurnTest extends BaseTest:

  val player = ScatanPlayer("a")

  "A turn" should "have a number" in {
    val turn = Turn(1, player)
    turn.number shouldBe 1
  }

  it should "have a player" in {
    val turn = Turn(1, player)
    turn.player shouldBe player
  }

  it should "not allow to have a number less than 1" in {
    assertThrows[IllegalArgumentException] {
      Turn(0, player)
    }
  }

  it should "be possible to create an initial turn" in {
    val turn = Turn.initial(player)
    turn.number shouldBe 1
    turn.player shouldBe player
  }
