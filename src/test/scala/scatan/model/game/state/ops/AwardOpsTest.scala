package scatan.model.game.state.ops

import scatan.model.components.*
import scatan.model.game.BaseScatanStateTest
import scatan.model.game.state.ScatanState
import scatan.model.game.state.ops.AwardOps.*
import scatan.model.game.state.ops.BuildingOps.assignBuilding
import scatan.model.game.state.ops.DevelopmentCardOps.assignDevelopmentCard
import scatan.model.game.state.ops.EmptySpotOps.{emptyRoadSpots, emptyStructureSpots}

class AwardOpsTest extends BaseScatanStateTest:

  "A State with Awards Ops" should "have awards initially not assigned" in {
    val state = ScatanState(threePlayers)
    state.awards(Award(AwardType.LongestRoad)) should be(None)
    state.awards(Award(AwardType.LargestArmy)) should be(None)
  }

  it should "assign a LongestRoad award if there are conditions" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val it = state.emptyRoadSpots.iterator
    val stateWithAward = for
      stateWithOneRoad <- state.assignRoadWithoutRule(it.next, player1)
      stateWithTwoRoad <- stateWithOneRoad.assignRoadWithoutRule(it.next, player1)
      stateWithThreeRoad <- stateWithTwoRoad.assignRoadWithoutRule(it.next, player1)
      stateWithFourRoad <- stateWithThreeRoad.assignRoadWithoutRule(it.next, player1)
      stateWithFiveRoad <- stateWithFourRoad.assignRoadWithoutRule(it.next, player1)
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
    val it = state.emptyRoadSpots.iterator
    val firstStateWithAward = for
      stateWithOneRoad <- state.assignRoadWithoutRule(it.next, player1)
      stateWithTwoRoad <- stateWithOneRoad.assignRoadWithoutRule(it.next, player1)
      stateWithThreeRoad <- stateWithTwoRoad.assignRoadWithoutRule(it.next, player1)
      stateWithFourRoad <- stateWithThreeRoad.assignRoadWithoutRule(it.next, player1)
      stateWithFiveRoad <- stateWithFourRoad.assignRoadWithoutRule(it.next, player1)
    yield stateWithFiveRoad
    val secondStateWithAward = for
      stateWithOneRoad <- firstStateWithAward.get.assignRoadWithoutRule(it.next, player2)
      stateWithTwoRoad <- stateWithOneRoad.assignRoadWithoutRule(it.next, player2)
      stateWithThreeRoad <- stateWithTwoRoad.assignRoadWithoutRule(it.next, player2)
      stateWithFourRoad <- stateWithThreeRoad.assignRoadWithoutRule(it.next, player2)
      stateWithFiveRoad <- stateWithFourRoad.assignRoadWithoutRule(it.next, player2)
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
    val it = state.emptyRoadSpots.iterator
    val firstStateWithAward = for
      stateWithOneRoad <- state.assignRoadWithoutRule(it.next, player1)
      stateWithTwoRoad <- stateWithOneRoad.assignRoadWithoutRule(it.next, player1)
      stateWithThreeRoad <- stateWithTwoRoad.assignRoadWithoutRule(it.next, player1)
      stateWithFourRoad <- stateWithThreeRoad.assignRoadWithoutRule(it.next, player1)
      stateWithFiveRoad <- stateWithFourRoad.assignRoadWithoutRule(it.next, player1)
    yield stateWithFiveRoad
    val secondStateWithAward = for
      stateWithOneRoad <- firstStateWithAward.get.assignRoadWithoutRule(it.next, player2)
      stateWithTwoRoad <- stateWithOneRoad.assignRoadWithoutRule(it.next, player2)
      stateWithThreeRoad <- stateWithTwoRoad.assignRoadWithoutRule(it.next, player2)
      stateWithFourRoad <- stateWithThreeRoad.assignRoadWithoutRule(it.next, player2)
      stateWithFiveRoad <- stateWithFourRoad.assignRoadWithoutRule(it.next, player2)
      stateWithSixRoad <- stateWithFiveRoad.assignRoadWithoutRule(it.next, player2)
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
