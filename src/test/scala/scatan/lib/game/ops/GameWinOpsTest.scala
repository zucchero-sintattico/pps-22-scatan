package scatan.lib.game.ops

import scatan.BaseTest
import scatan.lib.game.ops.GameWinOps.*
import scatan.lib.game.ops.RulesOps.withWinnerFunction
import scatan.lib.game.{EmptyDomain, Game}

class GameWinOpsTest extends BaseTest:

  import EmptyDomain.*
  given EmptyDomainRules = EmptyDomain.rules
  val players = Seq(Player("p1"), Player("p2"), Player("p3"))

  "A Game" should "expose a isOver method" in {
    val game = Game(players)
    game.isOver shouldBe false
    val endedGame = Game(players)(using EmptyDomain.rules.withWinnerFunction(_ => Some(players.head)))
    endedGame.isOver shouldBe true
  }

  it should "expose a winner method" in {
    val game = Game(players)
    game.winner shouldBe None
    val endedGame = Game(players)(using EmptyDomain.rules.withWinnerFunction(_ => Some(players.head)))
    endedGame.winner shouldBe Some(players.head)
  }
