package scatan.lib.game

import scatan.BaseTest
import scatan.model.GameMap

class GameTest extends BaseTest:

  import EmptyDomain.*
  val players = Seq(Player("A"), Player("B"), Player("C"))
  val gameMap = GameMap()
  given EmptyDomainRules = EmptyDomain.rules

  "A Game" should "exists given the rules" in {
    val game = Game(gameMap, players)
  }

  it should "have a state" in {
    val game = Game(gameMap, players)
    game.state should be(State())
  }

  it should "have a turn" in {
    val game = Game(gameMap, players)
    game.turn should be(Turn.initial(players.head))
  }

  it should "have a game status" in {
    val game = Game(gameMap, players)
    game.gameStatus should be(GameStatus(MyPhases.Game, Steps.Initial))
  }

  it should "have a players iterator" in {
    val game = Game(gameMap, players)
    game.playersIterator.toSeq should be(players.tail)
  }

  it should "contains the rules" in {
    val game = Game(gameMap, players)
    game.rules shouldBe a[EmptyDomainRules]
  }
