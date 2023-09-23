package scatan.model

class GameWithAwards extends BasicGameTest:
  "A Game with Awards" should "have awards initially not assigned" in {
    val game: Game = Game(players = threePlayers)
    game.awards(Award(AwardType.LongestRoad)) should be(Option.empty[Player])
    game.awards(Award(AwardType.LargestArmy)) should be(Option.empty[Player])
  }

  it should "assign a LongestRoad award if there are conditions" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val game2 = game.assignBuilding(Building(BuildingType.Road), player1)
    game2.awards(Award(AwardType.LongestRoad)) should be(Option.empty[(Player, Int)])
    val game3 = game2.assignBuilding(Building(BuildingType.Road), player1)
    game3.awards(Award(AwardType.LongestRoad)) should be(Option.empty[(Player, Int)])
    val game4 = game3.assignBuilding(Building(BuildingType.Road), player1)
    game4.awards(Award(AwardType.LongestRoad)) should be(Option.empty[(Player, Int)])
    val game5 = game4.assignBuilding(Building(BuildingType.Road), player1)
    game5.awards(Award(AwardType.LongestRoad)) should be(Option.empty[(Player, Int)])
    val game6 = game5.assignBuilding(Building(BuildingType.Road), player1)
    game6.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
  }

  it should "assign a LargestArmy award if there are conditions" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val game2 = game.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    game2.awards(Award(AwardType.LargestArmy)) should be(Option.empty[Player])
    val game3 = game2.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    game3.awards(Award(AwardType.LargestArmy)) should be(Option.empty[Player])
    val game4 = game3.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    game4.awards(Award(AwardType.LargestArmy)) should be(Some((player1, 3)))
    val game5 = game4.assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
    game5.awards(Award(AwardType.LargestArmy)) should be(Some((player1, 3)))
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
