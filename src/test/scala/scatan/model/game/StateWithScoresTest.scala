package scatan.model.game

import scatan.model.components.Score
import scatan.model.components.{AssignedBuildings, BuildingType}
import scatan.model.game.ScatanState
import scatan.model.game.state.BuildingCapacity.assignBuilding
import scatan.model.game.state.EmptySpotsManagement.emptyStructureSpot

class StateWithScoresTest extends BasicStateTest:

  "A State with Scores" should "have an empty scoreboard initially" in {
    val state = ScatanState(threePlayers)
    state.scores should be(Score.empty(threePlayers))
  }

  it should "increment score if assign a building" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyStructureSpot.iterator
    val stateWithSettlementPlaced = state.assignBuilding(it.next(), BuildingType.Settlement, player1)
    stateWithSettlementPlaced.scores(player1) should be(1)
    val stateWithSettlementAndCity =
      stateWithSettlementPlaced.assignBuilding(it.next(), BuildingType.City, player1)
    stateWithSettlementAndCity.scores(player1) should be(3)
    val stateWithSettlementCityAndRoad =
      stateWithSettlementAndCity.assignBuilding(it.next(), BuildingType.Road, player1)
    stateWithSettlementCityAndRoad.scores(player1) should be(3)
  }

  it should "increment score either if assign an award or a building" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyStructureSpot.iterator
    val stateWithSettlementAndAward = state
      .assignBuilding(it.next(), BuildingType.Settlement, player1)
      .assignBuilding(it.next(), BuildingType.Road, player1)
      .assignBuilding(it.next(), BuildingType.Road, player1)
      .assignBuilding(it.next(), BuildingType.Road, player1)
      .assignBuilding(it.next(), BuildingType.Road, player1)
      .assignBuilding(it.next(), BuildingType.Road, player1)
    stateWithSettlementAndAward.scores(player1) should be(2)
  }

  it should "recognize if there is a winner" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyStructureSpot.iterator
    val stateWithAWinner = state
      .assignBuilding(it.next(), BuildingType.Settlement, player1)
      .assignBuilding(it.next(), BuildingType.Settlement, player1)
      .assignBuilding(it.next(), BuildingType.Settlement, player1)
      .assignBuilding(it.next(), BuildingType.Settlement, player1)
      .assignBuilding(it.next(), BuildingType.Settlement, player1)
      .assignBuilding(it.next(), BuildingType.Settlement, player1)
      .assignBuilding(it.next(), BuildingType.Settlement, player1)
      .assignBuilding(it.next(), BuildingType.Settlement, player1)
      .assignBuilding(it.next(), BuildingType.Settlement, player1)
      .assignBuilding(it.next(), BuildingType.Settlement, player1)
    stateWithAWinner.winner should be(Some(player1))
  }
