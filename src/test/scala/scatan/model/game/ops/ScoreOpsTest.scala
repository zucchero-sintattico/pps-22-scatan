package scatan.model.game.ops

import scatan.model.components.{AssignedBuildings, BuildingType, Score}
import scatan.model.game.ScatanState
import scatan.model.game.ops.BuildingOps.assignBuilding
import scatan.model.game.ops.EmptySpotsOps.emptyStructureSpot
import scatan.model.game.ops.ScoreOps.*
import scatan.model.game.BaseScatanStateTest
import scatan.model.game.ops.EmptySpotsOps.emptyRoadSpot

class ScoreOpsTest extends BaseScatanStateTest:

  "A State with Scores Ops" should "have an empty scoreboard initially" in {
    val state = ScatanState(threePlayers)
    state.scores should be(Score.empty(threePlayers))
  }

  it should "increment score to one if assign a settlement" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyStructureSpot.iterator
    val stateWithSettlement = state.assignBuilding(it.next(), BuildingType.Settlement, player1)
    stateWithSettlement match
      case Some(state) =>
        state.scores(player1) should be(1)
      case None => fail("Settlement was not placed")
  }

  it should "increment score to two if assign a city" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyStructureSpot.iterator
    val spotToBuild = it.next()
    val stateWithSettlement = state.assignBuilding(spotToBuild, BuildingType.Settlement, player1)
    stateWithSettlement match
      case Some(state) =>
        val stateWithCity = state.assignBuilding(spotToBuild, BuildingType.City, player1)
        stateWithCity match
          case Some(state) =>
            state.scores(player1) should be(2)
          case None => fail("City was not placed")
      case None => fail("Settlement was not placed")
  }

  it should "not increment score if assign a road" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyRoadSpot.iterator
    val stateWithRoad = state.assignBuilding(it.next(), BuildingType.Road, player1)
    stateWithRoad match
      case Some(state) =>
        state.scores(player1) should be(0)
      case None => fail("Road was not placed")
  }

  it should "increment score either if assign an award or a building" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val roadSpotIterator = state.emptyRoadSpot.iterator
    val stateWithSettlementAndAward =
      for
        stateWithSettlement <- state.assignBuilding(state.emptyStructureSpot.head, BuildingType.Settlement, player1)
        oneRoadState <- stateWithSettlement.assignBuilding(roadSpotIterator.next, BuildingType.Road, player1)
        twoRoadState <- oneRoadState.assignBuilding(roadSpotIterator.next, BuildingType.Road, player1)
        threeRoadState <- twoRoadState.assignBuilding(roadSpotIterator.next, BuildingType.Road, player1)
        fourRoadState <- threeRoadState.assignBuilding(roadSpotIterator.next, BuildingType.Road, player1)
        fiveRoadState <- fourRoadState.assignBuilding(roadSpotIterator.next, BuildingType.Road, player1)
      yield fiveRoadState
    stateWithSettlementAndAward match
      case Some(state) =>
        state.scores(player1) should be(2)
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

    stateWithAWinner match
      case Some(state) =>
        state.scores(player1) should be(10)
        state.winner should be(Some(player1))
        state.isOver should be(true)
      case None => fail("Building was not placed")
  }
