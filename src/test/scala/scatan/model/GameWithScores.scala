package scatan.model

class GameWithScores extends BasicGameTest:

  "A Game with Scores" should "have an empty scoreboard initially" in {
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
