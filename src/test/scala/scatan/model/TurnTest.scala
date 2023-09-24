package scatan.model

import scatan.BaseTest
import scatan.model.game.{Player, Turn, next}

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

  it should "be nextable using players" in {
    val players = List(Player("a"), Player("b"), Player("c"))
    for i <- 1 to 3 do
      val turn = Turn(i, players(i - 1))
      turn.next(players) shouldBe Turn(i + 1, players(i % 3))
  }
