package scatan.lib.game.ops

import scatan.BaseTest
import scatan.lib.game.EmptyDomain.Actions.{NotPlayableAction, StartGame}
import scatan.lib.game.ops.GamePlayOps.{canPlay, play}
import scatan.lib.game.{EmptyDomain, Game}
import scatan.model.map.GameMap

class GamePlayOpsTest extends BaseTest:

  import EmptyDomain.*

  given EmptyDomainRules = EmptyDomain.rules
  val players = Seq(Player("p1"), Player("p2"), Player("p3"))
  val game = Game(GameMap(), players)

  "A Game" should "allow to check if an action is playable" in {
    game.canPlay(StartGame) shouldBe true
    game.canPlay(NotPlayableAction) shouldBe false
  }

  it should "allow to play an action given an instance of his effect on the game state" in {
    given Effect[StartGame.type, State] = Some(_)
    game.play(StartGame) should be(defined)
  }

  it should "not allow to play an action if it is not playable in the current game state even if the effect is provided" in {
    given Effect[NotPlayableAction.type, State] = Some(_)
    game.play(NotPlayableAction) shouldBe None
  }
