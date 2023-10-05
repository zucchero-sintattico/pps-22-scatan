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
    def rollDiceBruteForce(): ScatanState = ???
    // for
    //   stateAfterRollOne <- state.assignResourcesFromNumber(1)
    //   stateAfterRollTwo <- stateAfterRollOne.assignResourcesFromNumber(2)
    //   stateAfterRollThree <- stateAfterRollTwo.assignResourcesFromNumber(3)
    //   stateAfterRollFour <- stateAfterRollThree.assignResourcesFromNumber(4)
    //   stateAfterRollFive <- stateAfterRollFour.assignResourcesFromNumber(5)
    //   stateAfterRollSix <- stateAfterRollFive.assignResourcesFromNumber(6)
    // yield stateAfterRollSix

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
      .get
      .rollDiceBruteForce()
    stateWithResources.resourceCards(stateWithResources.players.head) should contain(ResourceCard(ResourceType.Sheep))
  }

  it should "assign two resource cards to the player who has a city on a spot having that resource terrain" in {
    val state = ScatanState(threePlayers)
    val hexagonWithSheep = state.gameMap.toContent.filter(_._2.terrain == ResourceType.Sheep).head._1
    val spotWhereToBuild = state.emptyStructureSpot.filter(_.contains(hexagonWithSheep)).head
    val stateWithResources = state
      .assignBuilding(spotWhereToBuild, BuildingType.City, state.players.head)
      .get
      .rollDiceBruteForce()
    stateWithResources
      .resourceCards(stateWithResources.players.head)
      .count(_.resourceType == ResourceType.Sheep) should be(2)
  }
