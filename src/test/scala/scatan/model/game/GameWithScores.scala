package scatan.model.game

class GameWithScores extends BasicGameTest:

  "A Game with Scores" should "have an empty scoreboard initially" in {
    val game: Game = Game(players = threePlayers)
    game.scores should be(Score.empty(threePlayers))
  }

  it should "increment score if assign a building" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val gameWithSettlementPlaced = game.assignBuilding(Building(BuildingType.Settlement), player1)
    gameWithSettlementPlaced.scores(player1) should be(1)
    val gameWithSettlementAndCity = gameWithSettlementPlaced.assignBuilding(Building(BuildingType.City), player1)
    gameWithSettlementAndCity.scores(player1) should be(3)
    val gameWithSettlementCityAndRoad =
      gameWithSettlementAndCity.assignBuilding(Building(BuildingType.Road), player1)
    gameWithSettlementCityAndRoad.scores(player1) should be(3)
  }

  it should "increment score either if assign an award or a building" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val gameWithSettlementAndAward = game
      .assignBuilding(Building(BuildingType.Settlement), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
    gameWithSettlementAndAward.scores(player1) should be(2)
  }

  it should "recognize if there is a winner" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val gameWithAWinner = game
      .assignBuilding(Building(BuildingType.Settlement), player1)
      .assignBuilding(Building(BuildingType.Settlement), player1)
      .assignBuilding(Building(BuildingType.Settlement), player1)
      .assignBuilding(Building(BuildingType.Settlement), player1)
      .assignBuilding(Building(BuildingType.Settlement), player1)
      .assignBuilding(Building(BuildingType.Settlement), player1)
      .assignBuilding(Building(BuildingType.Settlement), player1)
      .assignBuilding(Building(BuildingType.Settlement), player1)
      .assignBuilding(Building(BuildingType.Settlement), player1)
      .assignBuilding(Building(BuildingType.Settlement), player1)
    gameWithAWinner.winner should be(Some(player1))
  }
