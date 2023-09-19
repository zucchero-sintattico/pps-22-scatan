package scatan.model

import scatan.BaseTest

class GameTest extends BaseTest:

  private def players(n: Int): Seq[Player] =
    (1 to n).map(i => Player(s"Player $i"))

  val threePlayers = players(3)
  val fourPlayers = players(4)

  "A Game" should "exists" in {
    val game: Game = null
  }

  it should "have players" in {
    val game: Game = Game(players = threePlayers)
    game.players should be(threePlayers)
  }

  it should "take players" in {
    val game: Game = Game(players = threePlayers)
    game.players should be(threePlayers)
  }

  it should "not allow fewer than 3 players" in {
    for n <- 0 to 2
    yield assertThrows[IllegalArgumentException] {
      Game(players = players(n))
    }
  }

  it should "not allow more than 4 players" in {
    for n <- 5 to 10
    yield assertThrows[IllegalArgumentException] {
      Game(players = players(n))
    }
  }

  it should "have a current player" in {
    val game: Game = Game(players = threePlayers)
    game.currentPlayer should be(threePlayers.head)
  }

  it should "allow to change player" in {
    val game: Game = Game(players = threePlayers)
    game.currentPlayer should be(threePlayers.head)
    val game2 = game.changePlayer
    game2.currentPlayer should be(threePlayers.tail.head)
  }
