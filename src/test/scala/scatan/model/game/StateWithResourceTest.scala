package scatan.model.game

import scatan.model.components.ResourceCard
import scatan.model.map.Spot
import scatan.model.components.BuildingType
import scatan.lib.game.Player
import scatan.model.map.RoadSpot
import scatan.model.map.StructureSpot
import scatan.utils.UnorderedTriple
import scatan.model.map.HexagonInMap.layer

class StateWithResourceTest extends BasicStateTest:

  extension (state: ScatanState)
    def rollDiceBruteForce(): ScatanState =
      state
        .assignResourcesFromNumber(1)
        .assignResourcesFromNumber(2)
        .assignResourcesFromNumber(3)
        .assignResourcesFromNumber(4)
        .assignResourcesFromNumber(5)
        .assignResourcesFromNumber(6)
        .assignResourcesFromNumber(7)
        .assignResourcesFromNumber(8)
        .assignResourcesFromNumber(9)
        .assignResourcesFromNumber(10)
        .assignResourcesFromNumber(11)
        .assignResourcesFromNumber(12)

  "A State with Resources" should "have an empty resource card deck initially" in {
    val state = ScatanState(threePlayers)
    state.resourceCards should be(ResourceCard.empty(threePlayers))
  }

  it should "assign a resource card after a dice roll" in {
    val state = ScatanState(threePlayers)
    val stateWithResources = state
      .assignBuilding(state.emptySpots.getEmptyStructureSpots.head, BuildingType.Settlement, state.players.head)
      .rollDiceBruteForce()
    stateWithResources.resourceCards should not be ResourceCard.empty(threePlayers)
  }

  it should "assign a resource card to the player who has a settlement on a spot" in {
    val state = ScatanState(threePlayers)
    val stateWithResources = state
      .assignBuilding(state.emptySpots.getEmptyStructureSpots.head, BuildingType.Settlement, state.players.head)
      .rollDiceBruteForce()
    stateWithResources.resourceCards(state.players.head).size should be(1)
  }

  it should "assign two resource card to the player who has a city on a spot" in {
    val state = ScatanState(threePlayers)
    val stateWithResources = state
      .assignBuilding(state.emptySpots.getEmptyStructureSpots.head, BuildingType.City, state.players.head)
      .rollDiceBruteForce()
    println(stateWithResources.resourceCards)
    stateWithResources.resourceCards(state.players.head).size should be(2)
  }

  it should "assign two resource card to the player who has a settlement between two hexagons" in {
    val state = ScatanState(threePlayers)
    val spotWhereToBuild = state.emptySpots.getEmptyStructureSpots.filter(s => s.toSet.forall(h => h.layer <= 2)).head
    val hexagons = state.gameMap.toContent.filter(hex => spotWhereToBuild.contains(hex._1))
    val stateWithResources = state
      .assignBuilding(
        spotWhereToBuild,
        BuildingType.Settlement,
        state.players.head
      )
      .rollDiceBruteForce()
    stateWithResources.resourceCards(state.players.head).size should be(2)
  }
  it should "assign four resource card to the player who has a city between two hexagons" in {
    val state = ScatanState(threePlayers)
    val spotWhereToBuild = state.emptySpots.getEmptyStructureSpots.filter(s => s.toSet.forall(h => h.layer <= 2)).head
    val hexagons = state.gameMap.toContent.filter(hex => spotWhereToBuild.contains(hex._1))
    val stateWithResources = state
      .assignBuilding(
        spotWhereToBuild,
        BuildingType.City,
        state.players.head
      )
      .rollDiceBruteForce()
    stateWithResources.resourceCards(state.players.head).size should be(4)
  }
