package scatan.model

import scatan.BaseTest

class GameWithBuildings extends BasicGameTest:

  "A Game with Buildings" should "have empty buildings when game start" in {
    val game: Game = Game(players = threePlayers)
    game.buildings(threePlayers.head) should be(Seq.empty[Building])
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
