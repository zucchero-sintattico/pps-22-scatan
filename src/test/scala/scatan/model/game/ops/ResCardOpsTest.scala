package scatan.model.game.ops

import scatan.model.components.{BuildingType, ResourceCard, ResourceType}
import scatan.model.game.ops.BuildingOps.assignBuilding
import scatan.model.game.ops.CardOps.assignResourcesFromNumber
import scatan.model.game.ops.EmptySpotsOps.emptyStructureSpot
import scatan.model.map.HexagonInMap.layer
import scatan.model.map.{RoadSpot, Spot, StructureSpot}
import scatan.utils.UnorderedTriple
import scatan.model.game.BasicStateTest
import scatan.model.game.ScatanState

class ResCardOpsTest extends BasicStateTest:

  extension (state: ScatanState)
    /** This method assigns resources to players based on the number of the hexagons where their buildings are located.
      * All the numbers are tried, except the 7.
      *
      * @return
      */
    def tryEveryRollDices(): Option[ScatanState] =
      for
        rollTwoState <- state.assignResourcesFromNumber(2)
        rollThreeState <- rollTwoState.assignResourcesFromNumber(3)
        rollFourState <- rollThreeState.assignResourcesFromNumber(4)
        rollFiveState <- rollFourState.assignResourcesFromNumber(5)
        rollSixState <- rollFiveState.assignResourcesFromNumber(6)
        rollEightState <- rollSixState.assignResourcesFromNumber(8)
        rollNineState <- rollEightState.assignResourcesFromNumber(9)
        rollTenState <- rollNineState.assignResourcesFromNumber(10)
        rollElevenState <- rollTenState.assignResourcesFromNumber(11)
        rollTwelveState <- rollElevenState.assignResourcesFromNumber(12)
      yield rollTwelveState

  "A State with resource cards Ops" should "have an empty resource card deck initially" in {
    val state = ScatanState(threePlayers)
    state.resourceCards should be(ResourceCard.empty(threePlayers))
  }

  it should "assign a resource card to the player who has a settlement on a spot having that resource terrain" in {
    val state = ScatanState(threePlayers)
    val hexagonWithSheep = state.gameMap.toContent.filter(_._2.terrain == ResourceType.Sheep).head._1
    val spotWhereToBuild = state.emptyStructureSpot.filter(_.contains(hexagonWithSheep)).head
    val stateWithResources = for
      stateWithSettlement <- state.assignBuilding(spotWhereToBuild, BuildingType.Settlement, state.players.head)
      stateAfterRollDice <- stateWithSettlement.tryEveryRollDices()
    yield stateAfterRollDice
    stateWithResources match
      case Some(stateWithResources) =>
        stateWithResources.resourceCards(stateWithResources.players.head) should contain(
          ResourceCard(ResourceType.Sheep)
        )
      case None => fail("stateWithResources should be defined")
  }

  it should "assign two resource cards to the player who has a city on a spot having that resource terrain" in {
    val state = ScatanState(threePlayers)
    val hexagonWithSheep = state.gameMap.toContent.filter(_._2.terrain == ResourceType.Sheep).head._1
    val spotWhereToBuild = state.emptyStructureSpot.filter(_.contains(hexagonWithSheep)).head
    val stateWithResources = for
      stateWithSettlement <- state.assignBuilding(spotWhereToBuild, BuildingType.Settlement, state.players.head)
      stateWithCity <- stateWithSettlement.assignBuilding(spotWhereToBuild, BuildingType.City, state.players.head)
      stateWithResources <- stateWithCity.tryEveryRollDices()
    yield stateWithResources
    stateWithResources match
      case Some(stateWithResources) =>
        stateWithResources
          .resourceCards(stateWithResources.players.head)
          .filter(_.resourceType == ResourceType.Sheep) should have size 2
      case None => fail("stateWithResources should be defined")
  }
