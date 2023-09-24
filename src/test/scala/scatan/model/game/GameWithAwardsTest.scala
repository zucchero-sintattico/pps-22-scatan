package scatan.model.game

class GameWithAwardsTest extends BasicGameTest:
  "A Game with Awards" should "have awards initially not assigned" in {
    val game: Game = Game(players = threePlayers)
    game.awards(Award(AwardType.LongestRoad)) should be(Option.empty[Player])
    game.awards(Award(AwardType.LargestArmy)) should be(Option.empty[Player])
  }

  it should "assign a LongestRoad award if there are conditions" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val gameWithAwardReached = game
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
    gameWithAwardReached.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
  }

  it should "assign a LargestArmy award if there are conditions" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val gameWithAwardReached = game
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    gameWithAwardReached.awards(Award(AwardType.LargestArmy)) should be(Some((player1, 3)))
  }

  it should "not reassign LongestRoad if someone else reach same number of roads " in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val game2 = game
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
    val game3 = game2
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
    game2.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
    game3.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
  }

  it should "reassign LongestRoad if someone build more roads then the current award owner" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val game2 = game
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
    val game3 = game2
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
    game2.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
    game3.awards(Award(AwardType.LongestRoad)) should be(Some((player2, 6)))
  }

  it should "not reassign LargestArmy if someone else reach same number of knights " in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val game2 = game
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    val game3 = game2
      .assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
    game2.awards(Award(AwardType.LargestArmy)) should be(Some((player1, 3)))
    game3.awards(Award(AwardType.LargestArmy)) should be(Some((player1, 3)))
  }

  it should "reassign LargestArmy if someone build more knights then the current award owner" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val game2 = game
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    val game3 = game2
      .assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
    game2.awards(Award(AwardType.LargestArmy)) should be(Some((player1, 3)))
    game3.awards(Award(AwardType.LargestArmy)) should be(Some((player2, 4)))
  }
