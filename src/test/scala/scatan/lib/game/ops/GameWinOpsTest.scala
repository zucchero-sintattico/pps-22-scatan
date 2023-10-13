package scatan.lib.game.ops

import scatan.BaseTest
import scatan.lib.game.ops.GameWinOps.*
import scatan.lib.game.{EmptyDomain, Game}
import scatan.model.GameMap

class GameWinOpsTest extends BaseTest:

  import EmptyDomain.*
  given EmptyDomainRules = EmptyDomain.rules
  val players = Seq(Player("p1"), Player("p2"), Player("p3"))

  "A Game" should "expose a isOver method" in {
    val game = Game(GameMap(), players)
    game.isOver shouldBe false
  }

  it should "expose a winner method" in {
    val game = Game(GameMap(), players)
    game.winner shouldBe None
  }
