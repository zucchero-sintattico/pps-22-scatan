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
    val it = state.emptyRoadSpot.iterator
    val stateWithAward = for
      stateWithOneRoad <- state.assignBuilding(it.next, BuildingType.Road, player1)
      stateWithTwoRoad <- stateWithOneRoad.assignBuilding(it.next, BuildingType.Road, player1)
      stateWithThreeRoad <- stateWithTwoRoad.assignBuilding(it.next, BuildingType.Road, player1)
      stateWithFourRoad <- stateWithThreeRoad.assignBuilding(it.next, BuildingType.Road, player1)
      stateWithFiveRoad <- stateWithFourRoad.assignBuilding(it.next, BuildingType.Road, player1)
    yield stateWithFiveRoad
    stateWithAward match
      case Some(state) => state.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
      case None        => fail("stateWithAward should be defined")

  }

  it should "assign a LargestArmy award if there are conditions" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val stateWithAward = for
      stateWithOneKnight <- state.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      stateWithTwoKnight <- stateWithOneKnight.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      stateWithThreeKnight <- stateWithTwoKnight.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    yield stateWithThreeKnight
    stateWithAward match
      case Some(state) => state.awards(Award(AwardType.LargestArmy)) should be(Some((player1, 3)))
      case None        => fail("stateWithAward should be defined")
  }

  it should "not reassign LongestRoad if someone else reach same number of roads " in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val it = state.emptyRoadSpot.iterator
    val firstStateWithAward = for
      stateWithOneRoad <- state.assignBuilding(it.next, BuildingType.Road, player1)
      stateWithTwoRoad <- stateWithOneRoad.assignBuilding(it.next, BuildingType.Road, player1)
      stateWithThreeRoad <- stateWithTwoRoad.assignBuilding(it.next, BuildingType.Road, player1)
      stateWithFourRoad <- stateWithThreeRoad.assignBuilding(it.next, BuildingType.Road, player1)
      stateWithFiveRoad <- stateWithFourRoad.assignBuilding(it.next, BuildingType.Road, player1)
    yield stateWithFiveRoad
    val secondStateWithAward = for
      stateWithOneRoad <- firstStateWithAward.get.assignBuilding(it.next, BuildingType.Road, player2)
      stateWithTwoRoad <- stateWithOneRoad.assignBuilding(it.next, BuildingType.Road, player2)
      stateWithThreeRoad <- stateWithTwoRoad.assignBuilding(it.next, BuildingType.Road, player2)
      stateWithFourRoad <- stateWithThreeRoad.assignBuilding(it.next, BuildingType.Road, player2)
      stateWithFiveRoad <- stateWithFourRoad.assignBuilding(it.next, BuildingType.Road, player2)
    yield stateWithFiveRoad
    firstStateWithAward match
      case Some(state) => state.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
      case None        => fail("firstStateWithAward should be defined")

    secondStateWithAward match
      case Some(state) => state.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
      case None        => fail("secondStateWithAwardshould be defined")
  }

  it should "reassign LongestRoad if someone build more roads then the current award owner" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val it = state.emptyRoadSpot.iterator
    val firstStateWithAward = for
      stateWithOneRoad <- state.assignBuilding(it.next, BuildingType.Road, player1)
      stateWithTwoRoad <- stateWithOneRoad.assignBuilding(it.next, BuildingType.Road, player1)
      stateWithThreeRoad <- stateWithTwoRoad.assignBuilding(it.next, BuildingType.Road, player1)
      stateWithFourRoad <- stateWithThreeRoad.assignBuilding(it.next, BuildingType.Road, player1)
      stateWithFiveRoad <- stateWithFourRoad.assignBuilding(it.next, BuildingType.Road, player1)
    yield stateWithFiveRoad
    val secondStateWithAward = for
      stateWithOneRoad <- firstStateWithAward.get.assignBuilding(it.next, BuildingType.Road, player2)
      stateWithTwoRoad <- stateWithOneRoad.assignBuilding(it.next, BuildingType.Road, player2)
      stateWithThreeRoad <- stateWithTwoRoad.assignBuilding(it.next, BuildingType.Road, player2)
      stateWithFourRoad <- stateWithThreeRoad.assignBuilding(it.next, BuildingType.Road, player2)
      stateWithFiveRoad <- stateWithFourRoad.assignBuilding(it.next, BuildingType.Road, player2)
      stateWithSixRoad <- stateWithFiveRoad.assignBuilding(it.next, BuildingType.Road, player2)
    yield stateWithSixRoad
    firstStateWithAward match
      case Some(state) => state.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
      case None        => fail("firstStateWithAward should be defined")

    secondStateWithAward match
      case Some(state) => state.awards(Award(AwardType.LongestRoad)) should be(Some((player2, 6)))
      case None        => fail("secondStateWithAward should be defined")
  }

  it should "not reassign LargestArmy if someone else reach same number of knights " in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val stateWithAward = for
      oneKnightState <- state.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      twoKnightState <- oneKnightState.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      threeKnightState <- twoKnightState.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    yield threeKnightState
    val stateWithAwardDraw = for
      oneKnightState <- stateWithAward.get.assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
      twoKnightState <- oneKnightState.assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
      threeKnightState <- twoKnightState.assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
    yield threeKnightState
    stateWithAward match
      case Some(state) => state.awards(Award(AwardType.LargestArmy)) should be(Some((player1, 3)))
      case None        => fail("stateWithAward should be defined")
    stateWithAwardDraw match
      case Some(state) => state.awards(Award(AwardType.LargestArmy)) should be(Some((player1, 3)))
      case None        => fail("stateWithAwardDraw should be defined")
  }

  it should "reassign LargestArmy if someone build more knights then the current award owner" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val stateWithThreeKnights = for
      oneKnightState <- state.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      twoKnightState <- oneKnightState.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      threeKnightState <- twoKnightState.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    yield threeKnightState
    val stateWithFourKnights = for
      oneKnightState <- stateWithThreeKnights.get.assignDevelopmentCard(
        player2,
        DevelopmentCard(DevelopmentType.Knight)
      )
      twoKnightState <- oneKnightState.assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
      threeKnightState <- twoKnightState.assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
      fourKnightState <- threeKnightState.assignDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
    yield fourKnightState
    stateWithThreeKnights match
      case Some(state) => state.awards(Award(AwardType.LargestArmy)) should be(Some((player1, 3)))
      case None        => fail("stateWithThreeKnights should be defined")
    stateWithFourKnights match
      case Some(state) => state.awards(Award(AwardType.LargestArmy)) should be(Some((player2, 4)))
      case None        => fail("stateWithFourKnights should be defined")
  }
