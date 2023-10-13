package scatan.model.game.state.ops

import scatan.model.components.{BuildingType, ResourceCard, ResourceCards, ResourceType}
import scatan.model.game.BaseScatanStateTest
import scatan.model.game.state.ScatanState
import scatan.model.game.state.ops.BuildingOps.assignBuilding
import scatan.model.game.state.ops.CardOps.{
  assignResourceCard,
  assignResourcesAfterInitialPlacement,
  assignResourcesFromNumber,
  removeResourceCard
}
import scatan.model.game.state.ops.EmptySpotsOps.emptyStructureSpot
import scatan.model.map.HexagonInMap.layer
import scatan.model.map.{RoadSpot, Spot, StructureSpot}
import scatan.utils.UnorderedTriple

class ResCardOpsTest extends BaseScatanStateTest:

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
    state.resourceCards should be(ResourceCards.empty(threePlayers))
  }

  it should "assing a resource card to a player" in {
    val state = ScatanState(threePlayers)
    val player = state.players.head
    val resourceCard = ResourceCard(ResourceType.Brick)
    val stateWithResourceCard = state.assignResourceCard(player, resourceCard)
    stateWithResourceCard should be(
      Some(
        state.copy(
          resourceCards = state.resourceCards.updated(player, Seq(resourceCard))
        )
      )
    )
  }

  it should "remove a resource card from a player" in {
    val state = ScatanState(threePlayers)
    val player = state.players.head
    val resourceCard = ResourceCard(ResourceType.Brick)
    val stateWithResourceCard = state.assignResourceCard(player, resourceCard)
    val stateWithoutResourceCard = stateWithResourceCard.flatMap(_.removeResourceCard(player, resourceCard))
    stateWithoutResourceCard should be(
      Some(
        state.copy(
          resourceCards = state.resourceCards.updated(player, Seq.empty)
        )
      )
    )
  }

  it should "not remove a resource card from a player if the player does not have it" in {
    val state = ScatanState(threePlayers)
    val player = state.players.head
    val resourceCard = ResourceCard(ResourceType.Brick)
    val stateWithoutResourceCard = state.removeResourceCard(player, resourceCard)
    stateWithoutResourceCard should be(None)
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

  it should "assign only the resource card corresponding to the last building placed after initial phase" in {
    val state = ScatanState(threePlayers)
    val hexagonWithSheep = state.gameMap.toContent.filter(_._2.terrain == ResourceType.Brick).head._1
    val spotWhereToBuild = state.emptyStructureSpot.filter(_.contains(hexagonWithSheep)).iterator
    // simulate initial placement
    val stateWithBuildings = state
      .assignSettlmentWithoutRule(spotWhereToBuild.next(), state.players.head)
      .flatMap(_.assignSettlmentWithoutRule(spotWhereToBuild.next(), state.players.tail.head))
      .flatMap(_.assignSettlmentWithoutRule(spotWhereToBuild.next(), state.players.tail.tail.head))
      .flatMap(_.assignSettlmentWithoutRule(spotWhereToBuild.next(), state.players.tail.tail.head))
      .flatMap(_.assignSettlmentWithoutRule(spotWhereToBuild.next(), state.players.tail.head))
      .flatMap(_.assignSettlmentWithoutRule(spotWhereToBuild.next(), state.players.head))
    stateWithBuildings match
      case Some(state) =>
        state.assignResourcesAfterInitialPlacement match
          case Some(stateWithResources) =>
            stateWithResources.resourceCards(state.players.head).size should be <= 3 // max number of resources
            stateWithResources.resourceCards(state.players.tail.head).size should be <= 3 // max number of resources
            stateWithResources
              .resourceCards(state.players.tail.tail.head)
              .size should be <= 3 // max number of resources
          case None => fail("Resources not assigned")
      case None => fail("Buildings not assigned")
  }
