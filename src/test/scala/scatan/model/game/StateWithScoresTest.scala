package scatan.model.game

import scatan.model.components.{Building, BuildingType, Score}
import scatan.model.game.ScatanState

class StateWithScoresTest extends BasicStateTest:

  "A Game with Scores" should "have an empty scoreboard initially" in {
    val state = ScatanState(threePlayers)
    state.scores should be(Score.empty(threePlayers))
  }

  it should "increment score if assign a building" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val stateWithSettlementPlaced = state.assignBuilding(Building(BuildingType.Settlement), player1)
    stateWithSettlementPlaced.scores(player1) should be(1)
    val stateWithSettlementAndCity = stateWithSettlementPlaced.assignBuilding(Building(BuildingType.City), player1)
    stateWithSettlementAndCity.scores(player1) should be(3)
    val stateWithSettlementCityAndRoad =
      stateWithSettlementAndCity.assignBuilding(Building(BuildingType.Road), player1)
    stateWithSettlementCityAndRoad.scores(player1) should be(3)
  }

  it should "increment score either if assign an award or a building" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val stateWithSettlementAndAward = state
      .assignBuilding(Building(BuildingType.Settlement), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
    stateWithSettlementAndAward.scores(player1) should be(2)
  }

  it should "recognize if there is a winner" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val stateWithAWinner = state
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
    stateWithAWinner.winner should be(Some(player1))
  }