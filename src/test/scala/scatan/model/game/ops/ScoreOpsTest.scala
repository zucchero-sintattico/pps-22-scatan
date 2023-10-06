package scatan.model.game.ops

import scatan.model.components.{AssignedBuildings, BuildingType, Score}
import scatan.model.game.ScatanState
import scatan.model.game.ops.BuildingOps.assignBuilding
import scatan.model.game.ops.EmptySpotsOps.emptyStructureSpot
import scatan.model.game.ops.ScoreOps.*
import scatan.model.game.BasicStateTest
import scatan.model.game.ops.EmptySpotsOps.emptyRoadSpot

class ScoreOpsTest extends BasicStateTest:

  "A State with Scores Ops" should "have an empty scoreboard initially" in {
    val state = ScatanState(threePlayers)
    state.scores should be(Score.empty(threePlayers))
  }

  it should "increment score to one if assign a settlement" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyStructureSpot.iterator
    val stateWithSettlementPlaced = state.assignBuilding(it.next(), BuildingType.Settlement, player1)
    stateWithSettlementPlaced match
      case Some(stateWithSettlementPlaced) =>
        stateWithSettlementPlaced.scores(player1) should be(1)
      case None => fail("Settlement was not placed")
  }

  it should "increment score to two if assign a city" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyStructureSpot.iterator
    val spotToBuild = it.next()
    val stateWithSettlementPlaced = state.assignBuilding(spotToBuild, BuildingType.Settlement, player1)
    stateWithSettlementPlaced match
      case Some(stateWithSettlementPlaced) =>
        val stateWithCityPlaced = stateWithSettlementPlaced.assignBuilding(spotToBuild, BuildingType.City, player1)
        stateWithCityPlaced match
          case Some(stateWithCityPlaced) =>
            stateWithCityPlaced.scores(player1) should be(2)
          case None => fail("City was not placed")
      case None => fail("Settlement was not placed")
  }

  it should "not increment score if assign a road" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyRoadSpot.iterator
    val stateWithRoadPlaced = state.assignBuilding(it.next(), BuildingType.Road, player1)
    stateWithRoadPlaced match
      case Some(stateWithRoadPlaced) =>
        stateWithRoadPlaced.scores(player1) should be(0)
      case None => fail("Road was not placed")
  }

  it should "increment score either if assign an award or a building" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val roadSpotIterator = state.emptyRoadSpot.iterator
    val stateWithSettlementAndAward =
      for
        stateWithSettlement <- state.assignBuilding(state.emptyStructureSpot.head, BuildingType.Settlement, player1)
        stateWithOneRoad <- stateWithSettlement.assignBuilding(roadSpotIterator.next, BuildingType.Road, player1)
        stateWithTwoRoad <- stateWithOneRoad.assignBuilding(roadSpotIterator.next, BuildingType.Road, player1)
        stateWithThreeRoad <- stateWithTwoRoad.assignBuilding(roadSpotIterator.next, BuildingType.Road, player1)
        stateWithFourRoad <- stateWithThreeRoad.assignBuilding(roadSpotIterator.next, BuildingType.Road, player1)
        stateWithFiveRoad <- stateWithFourRoad.assignBuilding(roadSpotIterator.next, BuildingType.Road, player1)
      yield stateWithFiveRoad
    stateWithSettlementAndAward match
      case Some(stateWithSettlementAndAward) =>
        stateWithSettlementAndAward.scores(player1) should be(2)
      case None => fail("Building was not placed")
  }

  it should "recognize if there is a winner" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyStructureSpot.iterator
    val stateWithAWinner = for
      oneSettlementState <- state.assignBuilding(it.next, BuildingType.Settlement, player1)
      twoSettlementState <- oneSettlementState.assignBuilding(it.next, BuildingType.Settlement, player1)
      threeSettlementState <- twoSettlementState.assignBuilding(it.next, BuildingType.Settlement, player1)
      fourSettlementState <- threeSettlementState.assignBuilding(it.next, BuildingType.Settlement, player1)
      fiveSettlementState <- fourSettlementState.assignBuilding(it.next, BuildingType.Settlement, player1)
      sixSettlementState <- fiveSettlementState.assignBuilding(it.next, BuildingType.Settlement, player1)
      sevenSettlementState <- sixSettlementState.assignBuilding(it.next, BuildingType.Settlement, player1)
      eightSettlementState <- sevenSettlementState.assignBuilding(it.next, BuildingType.Settlement, player1)
      nineSettlementState <- eightSettlementState.assignBuilding(it.next, BuildingType.Settlement, player1)
      tenSettlementState <- nineSettlementState.assignBuilding(it.next, BuildingType.Settlement, player1)
    yield tenSettlementState

  }
