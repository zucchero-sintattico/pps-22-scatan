package scatan.model

import scatan.BaseTest

class GameTest extends BaseTest:

  private def players(n: Int): Seq[Player] =
    (1 to n).map(i => Player(s"Player $i"))

  val threePlayers = players(3)
  val fourPlayers = players(4)

  private def awards(): Awards =
    Map(Award(AwardType.LongestRoad) -> Option.empty[Player], Award(AwardType.LargestArmy) -> Option.empty[Player])

  "A Game" should "exists" in {
    val game: Game = null
  }

  it should "have players" in {
    val game: Game = Game(players = threePlayers, Award.getEmptyAwardMap())
    game.players should be(threePlayers)
  }

  it should "take players" in {
    val game: Game = Game(players = threePlayers, Award.getEmptyAwardMap())
    game.players should be(threePlayers)
  }

  it should "not allow fewer than 3 players" in {
    for n <- 0 to 2
    yield assertThrows[IllegalArgumentException] {
      Game(players = players(n), Award.getEmptyAwardMap())
    }
  }

  it should "not allow more than 4 players" in {
    for n <- 5 to 10
    yield assertThrows[IllegalArgumentException] {
      Game(players = players(n), Award.getEmptyAwardMap())
    }
  }

  it should "have a current player" in {
    val game: Game = Game(players = threePlayers, Award.getEmptyAwardMap())
    game.currentPlayer should be(threePlayers.head)
  }

  it should "allow to change player" in {
    val game: Game = Game(players = threePlayers, Award.getEmptyAwardMap())
    game.currentPlayer should be(threePlayers.head)
    val game2 = game.withNextPlayer
    game2.currentPlayer should be(threePlayers.drop(1).head)
  }

  it should "allow to change player in a round-robin fashion" in {
    val game: Game = Game(players = threePlayers, Award.getEmptyAwardMap())
    game.currentPlayer should be(threePlayers.head)
    val game2 = game.withNextPlayer
    game2.currentPlayer should be(threePlayers.tail.head)
    val game3 = game2.withNextPlayer
    game3.currentPlayer should be(threePlayers.tail.tail.head)
    val game4 = game3.withNextPlayer
    game4.currentPlayer should be(threePlayers.head)
  }

  it should "have awards initially not assigned" in {
    val game: Game = Game(players = threePlayers, Award.getEmptyAwardMap())
    game.awards(Award(AwardType.LongestRoad)) should be(Option.empty[Player])
    game.awards(Award(AwardType.LargestArmy)) should be(Option.empty[Player])
  }

  it should "allow to assign awards" in {
    val game: Game = Game(players = threePlayers, Award.getEmptyAwardMap())
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val game2 = game.assignAward(Award(AwardType.LongestRoad), player1)
    game2.awards(Award(AwardType.LongestRoad)) should be(Some(player1))
    game2.awards(Award(AwardType.LargestArmy)) should be(Option.empty[Player])
    val game3 = game2.assignAward(Award(AwardType.LargestArmy), player2)
    game3.awards(Award(AwardType.LongestRoad)) should be(Some(player1))
    game3.awards(Award(AwardType.LargestArmy)) should be(Some(player2))
  }
