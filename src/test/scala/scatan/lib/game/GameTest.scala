package scatan.lib.game

import scatan.BaseTest

class GameTest extends BaseTest:

  import EmptyDomain.*
  val players = Seq(Player("A"), Player("B"), Player("C"))
  given EmptyDomainRules = EmptyDomain.rules

  "A Game" should "exists given the rules" in {
    val game = Game(players, State())
  }

  it should "have a state" in {
    val game = Game(players, State())
    game.state should be(State())
  }

  it should "have a turn" in {
    val game = Game(players, State())
    game.turn should be(Turn.initial(players.head))
  }

  it should "have a game status" in {
    val game = Game(players, State())
    game.gameStatus should be(GameStatus(MyPhases.Game, Steps.Initial))
  }

  it should "have a players iterator" in {
    val game = Game(players, State())
    game.playersIterator.toSeq should be(players.tail)
  }

  it should "contains the rules" in {
    val game = Game(players, State())
    game.rules shouldBe a[EmptyDomainRules]
  }
