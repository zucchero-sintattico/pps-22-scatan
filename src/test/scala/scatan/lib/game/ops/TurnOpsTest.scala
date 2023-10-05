package scatan.lib.game.ops

import scatan.BaseTest
import scatan.lib.game.Turn
import scatan.lib.game.ops.TurnOps.next
import scatan.model.game.config.ScatanPlayer

class TurnOpsTest extends BaseTest:

  val player = ScatanPlayer("a")

  "A Turn" should "be nextable passing next player" in {
    val turn = Turn(1, player)
    val nextTurn = turn.next(ScatanPlayer("b"))
    nextTurn.number shouldBe 2
    nextTurn.player shouldBe ScatanPlayer("b")
  }
