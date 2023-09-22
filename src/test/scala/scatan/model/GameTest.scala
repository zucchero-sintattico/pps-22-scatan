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

  it should "have awards initially not assigned" in {
    val game: Game = Game(players = threePlayers)
    game.awards(Award(AwardType.LongestRoad)) should be(Option.empty[Player])
    game.awards(Award(AwardType.LargestArmy)) should be(Option.empty[Player])
  }

  it should "allow to assign awards" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val game2 = game.assignAward(Award(AwardType.LongestRoad), player1)
    game2.awards(Award(AwardType.LongestRoad)) should be(Some(player1))
    game2.awards(Award(AwardType.LargestArmy)) should be(Option.empty[Player])
    val game3 = game2.assignAward(Award(AwardType.LargestArmy), player2)
    game3.awards(Award(AwardType.LongestRoad)) should be(Some(player1))
    game3.awards(Award(AwardType.LargestArmy)) should be(Some(player2))
  }

  it should "have an empty scoreboard initially" in {
    val game: Game = Game(players = threePlayers)
    game.scores should be(Score.EmptyScores(threePlayers))
  }

  it should "increase player score when assign him an award" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val game2 = game.assignAward(Award(AwardType.LongestRoad), player1)
    game.scores(player1) should be(0)
    val game3 = game2.assignAward(Award(AwardType.LargestArmy), player1)
    game2.scores(player1) should be(1)
    game3.scores(player1) should be(2)
  }
