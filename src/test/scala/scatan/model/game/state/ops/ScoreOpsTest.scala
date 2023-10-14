package scatan.model.game.state.ops

import scatan.model.components.*
import scatan.model.game.BaseScatanStateTest
import scatan.model.game.state.ScatanState
import scatan.model.game.state.ops.BuildingOps.assignBuilding
import scatan.model.game.state.ops.DevelopmentCardOps.assignDevelopmentCard
import scatan.model.game.state.ops.EmptySpotOps.{emptyRoadSpots, emptyStructureSpots}
import scatan.model.game.state.ops.ResourceCardOps.*
import scatan.model.game.state.ops.ScoreOps.*

class ScoreOpsTest extends BaseScatanStateTest:

  "A State with scores Ops" should "have an empty scoreboard initially" in {
    val state = ScatanState(threePlayers)
    state.scores should be(Scores.empty(threePlayers))
  }

  it should "increment score to one if assign a settlement" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyStructureSpots.iterator
    val stateWithSettlement = state.assignBuilding(it.next(), BuildingType.Settlement, player1)
    stateWithSettlement match
      case Some(state) =>
        state.scores(player1) should be(1)
      case None => fail("Settlement was not placed")
  }

  it should "increment score to two if assign a city" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyStructureSpots.iterator
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
    val it = state.emptyRoadSpots.iterator
    val stateWithRoad = state.assignRoadWithoutRule(it.next(), player1)
    stateWithRoad match
      case Some(state) =>
        state.scores(player1) should be(0)
      case None => fail("Road was not placed")
  }

  it should "increment score if player has a victory point" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val stateWithVictoryPoint = state.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.VictoryPoint))
    stateWithVictoryPoint match
      case Some(state) =>
        state.scores(player1) should be(1)
      case None => fail("Victory point was not placed")
  }

  it should "increment score if assign an award a building and a victory point" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val roadSpotIterator = state.emptyRoadSpots.iterator
    val stateWithSettlementAndAward =
      for
        stateWithSettlement <- state.assignBuilding(state.emptyStructureSpots.head, BuildingType.Settlement, player1)
        oneRoadState <- stateWithSettlement.assignRoadWithoutRule(roadSpotIterator.next, player1)
        twoRoadState <- oneRoadState.assignRoadWithoutRule(roadSpotIterator.next, player1)
        threeRoadState <- twoRoadState.assignRoadWithoutRule(roadSpotIterator.next, player1)
        fourRoadState <- threeRoadState.assignRoadWithoutRule(roadSpotIterator.next, player1)
        fiveRoadState <- fourRoadState.assignRoadWithoutRule(roadSpotIterator.next, player1)
        stateWithVictoryPoint <- fiveRoadState.assignDevelopmentCard(
          player1,
          DevelopmentCard(DevelopmentType.VictoryPoint)
        )
      yield stateWithVictoryPoint
    stateWithSettlementAndAward match
      case Some(state) =>
        state.scores(player1) should be(3)
      case None => fail("Building was not placed")
  }

  it should "recognize if there is a winner" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyStructureSpots.iterator
    val stateWithAWinner = for
      oneSettlementState <- state.assignSettlmentWithoutRule(it.next, player1)
      twoSettlementState <- oneSettlementState.assignSettlmentWithoutRule(it.next, player1)
      threeSettlementState <- twoSettlementState.assignSettlmentWithoutRule(it.next, player1)
      fourSettlementState <- threeSettlementState.assignSettlmentWithoutRule(it.next, player1)
      fiveSettlementState <- fourSettlementState.assignSettlmentWithoutRule(it.next, player1)
      sixSettlementState <- fiveSettlementState.assignSettlmentWithoutRule(it.next, player1)
      sevenSettlementState <- sixSettlementState.assignSettlmentWithoutRule(it.next, player1)
      eightSettlementState <- sevenSettlementState.assignSettlmentWithoutRule(it.next, player1)
      nineSettlementState <- eightSettlementState.assignSettlmentWithoutRule(it.next, player1)
      tenSettlementState <- nineSettlementState.assignSettlmentWithoutRule(it.next, player1)
    yield tenSettlementState

    stateWithAWinner match
      case Some(state) =>
        state.scores(player1) should be(10)
        state.winner should be(Some(player1))
        state.isOver should be(true)
      case None => fail("Building was not placed")
  }
