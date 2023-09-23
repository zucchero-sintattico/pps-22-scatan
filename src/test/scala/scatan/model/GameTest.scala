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

  it should "assign a LongestRoad award if there are conditions" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val game2 = game.assignBuilding(Building(BuildingType.Road), player1)
    game2.awards(Award(AwardType.LongestRoad)) should be(Option.empty[Player])
    val game3 = game2.assignBuilding(Building(BuildingType.Road), player1)
    game3.awards(Award(AwardType.LongestRoad)) should be(Option.empty[Player])
    val game4 = game3.assignBuilding(Building(BuildingType.Road), player1)
    game4.awards(Award(AwardType.LongestRoad)) should be(Option.empty[Player])
    val game5 = game4.assignBuilding(Building(BuildingType.Road), player1)
    game5.awards(Award(AwardType.LongestRoad)) should be(Option.empty[Player])
    val game6 = game5.assignBuilding(Building(BuildingType.Road), player1)
    game6.awards(Award(AwardType.LongestRoad)) should be(Some(player1))
    val game7 = game6.assignBuilding(Building(BuildingType.Road), player2)
    game7.awards(Award(AwardType.LongestRoad)) should be(Some(player1))
  }

  it should "have an empty building map initially" in {
    val game: Game = Game(players = threePlayers)
    game.buildings should be(Building.empty(threePlayers))
  }

  it should "allow to assign buildings" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val game2 = game.assignBuilding(Building(BuildingType.Settlement), player1)
    game2.buildings(player1) should be(Seq(Building(BuildingType.Settlement)))
    game2.buildings(player2) should be(Seq.empty[Building])
    val game3 = game2.assignBuilding(Building(BuildingType.City), player1)
    game3.buildings(player1) should be(Seq(Building(BuildingType.Settlement), Building(BuildingType.City)))
    game3.buildings(player2) should be(Seq.empty[Building])
    val game4 = game3.assignBuilding(Building(BuildingType.Road), player2)
    game4.buildings(player1) should be(Seq(Building(BuildingType.Settlement), Building(BuildingType.City)))
    game4.buildings(player2) should be(Seq(Building(BuildingType.Road)))
  }

  it should "have an empty scoreboard initially" in {
    val game: Game = Game(players = threePlayers)
    game.scores should be(Score.empty(threePlayers))
  }

  it should "increment score if assign a building" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val game2 = game.assignBuilding(Building(BuildingType.Settlement), player1)
    game2.scores(player1) should be(1)
    val game3 = game2.assignBuilding(Building(BuildingType.City), player1)
    game3.scores(player1) should be(3)
    val game4 = game3.assignBuilding(Building(BuildingType.Road), player1)
    game4.scores(player1) should be(3)
  }

  it should "increment score either if assign an award or a building" in {}
