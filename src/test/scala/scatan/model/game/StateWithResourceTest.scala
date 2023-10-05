package scatan.model.game

import scatan.model.components.{BuildingType, ResourceCard, ResourceType}
import scatan.model.game.ops.BuildingOps.assignBuilding
import scatan.model.game.ops.CardOps.assignResourcesFromNumber
import scatan.model.game.ops.EmptySpotsOps.emptyStructureSpot
import scatan.model.map.HexagonInMap.layer
import scatan.model.map.{RoadSpot, Spot, StructureSpot}
import scatan.utils.UnorderedTriple

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

  it should "assign a resource card to the player who has a settlement on a spot having that resource terrain" in {
    val state = ScatanState(threePlayers)
    val hexagonWithSheep = state.gameMap.toContent.filter(_._2.terrain == ResourceType.Sheep).head._1
    val spotWhereToBuild = state.emptyStructureSpot.filter(_.contains(hexagonWithSheep)).head
    val stateWithResources = state
      .assignBuilding(spotWhereToBuild, BuildingType.Settlement, state.players.head)
      .rollDiceBruteForce()
    stateWithResources.resourceCards(stateWithResources.players.head) should contain(ResourceCard(ResourceType.Sheep))
  }

  it should "assign two resource cards to the player who has a city on a spot having that resource terrain" in {
    val state = ScatanState(threePlayers)
    val hexagonWithSheep = state.gameMap.toContent.filter(_._2.terrain == ResourceType.Sheep).head._1
    val spotWhereToBuild = state.emptyStructureSpot.filter(_.contains(hexagonWithSheep)).head
    val stateWithResources = state
      .assignBuilding(spotWhereToBuild, BuildingType.City, state.players.head)
      .rollDiceBruteForce()
    stateWithResources
      .resourceCards(stateWithResources.players.head)
      .count(_.resourceType == ResourceType.Sheep) should be(2)
  }