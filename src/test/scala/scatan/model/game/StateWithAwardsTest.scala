package scatan.model.game

import scatan.lib.game.Player
import scatan.model.components.*
import scatan.model.game.ScatanState

class StateWithAwardsTest extends BasicStateTest:

  "A State with Awards" should "have awards initially not assigned" in {
    val state = ScatanState(threePlayers)
    state.awards(Award(AwardType.LongestRoad)) shouldBe None
    state.awards(Award(AwardType.LargestArmy)) shouldBe None
  }

  it should "assign a LongestRoad award if there are conditions" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val stateWithAwardReached = state
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
    stateWithAwardReached.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
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
    val state2 = state
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
    val state3 = state2
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
    state2.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
    state3.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
  }

  it should "reassign LongestRoad if someone build more roads then the current award owner" in {
    val state = ScatanState(threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val state2 = state
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
      .assignBuilding(Building(BuildingType.Road), player1)
    val state3 = state2
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
      .assignBuilding(Building(BuildingType.Road), player2)
    state2.awards(Award(AwardType.LongestRoad)) should be(Some((player1, 5)))
    state3.awards(Award(AwardType.LongestRoad)) should be(Some((player2, 6)))
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
