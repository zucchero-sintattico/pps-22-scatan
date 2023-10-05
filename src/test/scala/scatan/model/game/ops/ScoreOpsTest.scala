package scatan.model.game.ops

import scatan.model.components.{AssignedBuildings, BuildingType, Score}
import scatan.model.game.ScatanState
import scatan.model.game.ops.BuildingOps.assignBuilding
import scatan.model.game.ops.EmptySpotsOps.emptyStructureSpot
import scatan.model.game.ops.ScoreOps.*
import scatan.model.game.BasicStateTest

class ScoreOpsTest extends BasicStateTest:

  "A State with Scores Ops" should "have an empty scoreboard initially" in {
    val state = ScatanState(threePlayers)
    state.scores should be(Score.empty(threePlayers))
  }

  it should "increment score if assign a building" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyStructureSpot.iterator
    val stateWithSettlementPlaced = state.assignBuilding(it.next(), BuildingType.Settlement, player1)
    stateWithSettlementPlaced match
      case Some(stateWithSettlementPlaced) =>
        stateWithSettlementPlaced.scores(player1) should be(1)
      case None => fail("Settlement was not placed")

  }

  it should "increment score either if assign an award or a building" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyStructureSpot.iterator
    val stateWithSettlementAndAward =
      for
        stateWithSettlement <- state.assignBuilding(it.next(), BuildingType.Settlement, player1)
        stateWithOneRoad <- stateWithSettlement.assignBuilding(it.next(), BuildingType.Road, player1)
        stateWithTwoRoad <- stateWithOneRoad.assignBuilding(it.next(), BuildingType.Road, player1)
        stateWithThreeRoad <- stateWithTwoRoad.assignBuilding(it.next(), BuildingType.Road, player1)
        stateWithFourRoad <- stateWithThreeRoad.assignBuilding(it.next(), BuildingType.Road, player1)
        stateWithFiveRoad <- stateWithFourRoad.assignBuilding(it.next(), BuildingType.Road, player1)
      yield stateWithFiveRoad
    stateWithSettlementAndAward match
      case Some(stateWithSettlementAndAward) =>
        stateWithSettlementAndAward.scores(player1) should be(2)
      case None => fail("Settlement was not placed")
  }

  it should "recognize if there is a winner" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyStructureSpot.iterator
    val stateWithAWinner = for
      stateWithOneSettlement <- state.assignBuilding(it.next(), BuildingType.Settlement, player1)
      stateWithTwoSettlement <- stateWithOneSettlement.assignBuilding(it.next(), BuildingType.Settlement, player1)
      stateWithThreeSettlement <- stateWithTwoSettlement.assignBuilding(it.next(), BuildingType.Settlement, player1)
      stateWithFourSettlement <- stateWithThreeSettlement.assignBuilding(it.next(), BuildingType.Settlement, player1)
      stateWithFiveSettlement <- stateWithFourSettlement.assignBuilding(it.next(), BuildingType.Settlement, player1)
      stateWithSixSettlement <- stateWithFiveSettlement.assignBuilding(it.next(), BuildingType.Settlement, player1)
      stateWithSevenSettlement <- stateWithSixSettlement.assignBuilding(it.next(), BuildingType.Settlement, player1)
      stateWithEightSettlement <- stateWithSevenSettlement.assignBuilding(it.next(), BuildingType.Settlement, player1)
      stateWithNineSettlement <- stateWithEightSettlement.assignBuilding(it.next(), BuildingType.Settlement, player1)
      stateWithTenSettlement <- stateWithNineSettlement.assignBuilding(it.next(), BuildingType.Settlement, player1)
    yield stateWithTenSettlement

  }
