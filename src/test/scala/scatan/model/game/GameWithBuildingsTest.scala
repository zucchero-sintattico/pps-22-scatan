package scatan.model.game

import scatan.BaseTest

class GameWithBuildingsTest extends BasicGameTest:

  "A Game with Buildings" should "have empty buildings when game start" in {
    val game: Game = Game(players = threePlayers)
    game.buildings(threePlayers.head) should be(Seq.empty[Building])
  }

  it should "not allow to assign buildings if the player has't the necessary resources" in {
    val game: Game = Game(players = threePlayers)
    val game2 = game.build(Building(BuildingType.Settlement), threePlayers.head)
    game.buildings(threePlayers.head) should be(Seq.empty[Building])
    game2.buildings(threePlayers.head) should be(Seq.empty[Building])
  }

  it should "allow to assign buildings if the player has the necessary resources" in {
    val game: Game = Game(players = threePlayers)
    val gameWithBuilding = game
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Sheep))
      .build(Building(BuildingType.Settlement), threePlayers.head)
    game.buildings(threePlayers.head) should be(Seq.empty[Building])
    gameWithBuilding.buildings(threePlayers.head) should be(Seq(Building(BuildingType.Settlement)))
  }

  it should "consume the resources when a building is assigned" in {
    val game: Game = Game(players = threePlayers)
    val gameWithBuilding = game
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Sheep))
      .build(Building(BuildingType.Settlement), threePlayers.head)
    gameWithBuilding.resourceCards(threePlayers.head) should be(Seq.empty[ResourceCard])
  }
