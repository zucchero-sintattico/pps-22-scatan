package scatan.model.game.ops

import scatan.model.components.*
import scatan.model.game.ops.BuildingOps.assignBuilding
import scatan.model.game.ops.CardOps.assignDevelopmentCard
import scatan.model.game.ops.EmptySpotsOps.{emptyRoadSpot, emptyStructureSpot}
import scatan.model.game.ops.AwardOps.*
import scatan.model.game.BasicStateTest
import scatan.model.game.ScatanState

class AwardOpsTest extends BasicStateTest:

  "A State with Awards Ops" should "have awards initially not assigned" in {
    val state = ScatanState(threePlayers)
    state.awards(Award(AwardType.LongestRoad)) should be(None)
    state.awards(Award(AwardType.LargestArmy)) should be(None)
  }

  it should "assign a LongestRoad award if there are conditions" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyStructureSpot.iterator
    val stateWithAwardReached = for
      stateWithOneRoad <- state.assignBuilding(it.next(), BuildingType.Road, player1)
      stateWithTwoRoad <- stateWithOneRoad.assignBuilding(it.next(), BuildingType.Road, player1)
      stateWithThreeRoad <- stateWithTwoRoad.assignBuilding(it.next(), BuildingType.Road, player1)
      stateWithFourRoad <- stateWithThreeRoad.assignBuilding(it.next(), BuildingType.Road, player1)
      stateWithFiveRoad <- stateWithFourRoad.assignBuilding(it.next(), BuildingType.Road, player1)
    yield stateWithFiveRoad
    stateWithAwardReached match
      case Some(state) => state.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
      case None        => fail("stateWithAwardReached should be defined")

  }

  it should "assign a LargestArmy award if there are conditions" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val stateWithAwardReached = state
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    stateWithAwardReached.awards(Award(AwardType.LargestArmy)) should be(Some((player1, 3)))
  }

  it should "not reassign LongestRoad if someone else reach same number of roads " in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val it = state.emptyRoadSpot.iterator
    val firstStateWithAwardReached = for
      stateWithOneRoad <- state.assignBuilding(it.next(), BuildingType.Road, player1)
      stateWithTwoRoad <- stateWithOneRoad.assignBuilding(it.next(), BuildingType.Road, player1)
      stateWithThreeRoad <- stateWithTwoRoad.assignBuilding(it.next(), BuildingType.Road, player1)
      stateWithFourRoad <- stateWithThreeRoad.assignBuilding(it.next(), BuildingType.Road, player1)
      stateWithFiveRoad <- stateWithFourRoad.assignBuilding(it.next(), BuildingType.Road, player1)
    yield stateWithFiveRoad
    val secondStateWithAwardReached = for
      stateWithOneRoad <- firstStateWithAwardReached.get.assignBuilding(it.next(), BuildingType.Road, player2)
      stateWithTwoRoad <- stateWithOneRoad.assignBuilding(it.next(), BuildingType.Road, player2)
      stateWithThreeRoad <- stateWithTwoRoad.assignBuilding(it.next(), BuildingType.Road, player2)
      stateWithFourRoad <- stateWithThreeRoad.assignBuilding(it.next(), BuildingType.Road, player2)
      stateWithFiveRoad <- stateWithFourRoad.assignBuilding(it.next(), BuildingType.Road, player2)
    yield stateWithFiveRoad
    firstStateWithAwardReached match
      case Some(state) => state.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
      case None        => fail("firstStateWithAwardReached should be defined")

    secondStateWithAwardReached match
      case Some(state) => state.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
      case None        => fail("secondStateWithAwardReached should be defined")
  }

  it should "reassign LongestRoad if someone build more roads then the current award owner" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val it = state.emptyRoadSpot.iterator
    val firstStateWithAwardReached = for
      stateWithOneRoad <- state.assignBuilding(it.next(), BuildingType.Road, player1)
      stateWithTwoRoad <- stateWithOneRoad.assignBuilding(it.next(), BuildingType.Road, player1)
      stateWithThreeRoad <- stateWithTwoRoad.assignBuilding(it.next(), BuildingType.Road, player1)
      stateWithFourRoad <- stateWithThreeRoad.assignBuilding(it.next(), BuildingType.Road, player1)
      stateWithFiveRoad <- stateWithFourRoad.assignBuilding(it.next(), BuildingType.Road, player1)
    yield stateWithFiveRoad
    val secondStateWithAwardReached = for
      stateWithOneRoad <- firstStateWithAwardReached.get.assignBuilding(it.next(), BuildingType.Road, player2)
      stateWithTwoRoad <- stateWithOneRoad.assignBuilding(it.next(), BuildingType.Road, player2)
      stateWithThreeRoad <- stateWithTwoRoad.assignBuilding(it.next(), BuildingType.Road, player2)
      stateWithFourRoad <- stateWithThreeRoad.assignBuilding(it.next(), BuildingType.Road, player2)
      stateWithFiveRoad <- stateWithFourRoad.assignBuilding(it.next(), BuildingType.Road, player2)
      stateWithSixRoad <- stateWithFiveRoad.assignBuilding(it.next(), BuildingType.Road, player2)
    yield stateWithSixRoad
    firstStateWithAwardReached match
      case Some(state) => state.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
      case None        => fail("firstStateWithAwardReached should be defined")

    secondStateWithAwardReached match
      case Some(state) => state.awards(Award(AwardType.LongestRoad)) should be(Some((player2, 6)))
      case None        => fail("secondStateWithAwardReached should be defined")
  }

  it should "not reassign LargestArmy if someone else reach same number of knights " in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val state2 = state
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    val state3 = state2
      .assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
    state2.awards(Award(AwardType.LargestArmy)) should be(Some((player1, 3)))
    state3.awards(Award(AwardType.LargestArmy)) should be(Some((player1, 3)))
  }

  it should "reassign LargestArmy if someone build more knights then the current award owner" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val state2 = state
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    val state3 = state2
      .assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
    state2.awards(Award(AwardType.LargestArmy)) should be(Some((player1, 3)))
    state3.awards(Award(AwardType.LargestArmy)) should be(Some((player2, 4)))
  }
