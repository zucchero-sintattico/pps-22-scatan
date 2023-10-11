package scatan.model.game.ops

import scatan.model.components.*
import scatan.model.game.{BaseScatanStateTest, ScatanState}
import scatan.model.game.ops.BuildingOps.{assignBuilding, build}
import scatan.model.game.ops.CardOps.assignResourceCard
import scatan.model.game.ops.EmptySpotsOps.{emptyRoadSpot, emptyStructureSpot}
import scatan.model.map.{RoadSpot, StructureSpot}

class BuildingOpsTest extends BaseScatanStateTest:

  private def spotToBuildStructure(state: ScatanState): StructureSpot =
    state.emptyStructureSpot.head

  private def spotToBuildRoad(state: ScatanState): RoadSpot =
    state.emptyRoadSpot.head

  "A State with buildings Ops" should "have empty buildings when state start" in {
    val state = ScatanState(threePlayers)
    state.assignedBuildings.keySet should have size 0
  }

  it should "permit to assign a road" in {
    val state = ScatanState(threePlayers)
    state
      .assignBuilding(spotToBuildRoad(state), BuildingType.Road, threePlayers.head) match
      case Some(state) => state.assignedBuildings should have size 1
      case None        => fail("state should be defined")
  }

  it should "permit to assign a settlement" in {
    val state = ScatanState(threePlayers)
    state
      .assignBuilding(spotToBuildStructure(state), BuildingType.Settlement, threePlayers.head) match
      case Some(state) => state.assignedBuildings should have size 1
      case None        => fail("state should be defined")
  }

  it should "not permit to assign a settlement if the spot is not empty" in {
    val state = ScatanState(threePlayers)
    val spot = spotToBuildStructure(state)
    val stateWithBuilding = for
      stateWithWood <- state.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      stateWithBrick <- stateWithWood.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      stateWithSheep <- stateWithBrick.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Sheep))
      stateWithSettlement <- stateWithSheep.build(spot, BuildingType.Settlement, threePlayers.head)
    yield stateWithSettlement
    stateWithBuilding match
      case Some(state) =>
        state.assignedBuildings(spot) should be(AssignmentInfo(threePlayers.head, BuildingType.Settlement))
        state
          .assignBuilding(spot, BuildingType.Settlement, threePlayers.head) match
          case Some(_) => fail("state should not be defined")
          case None    => succeed

      case None => fail("stateWithBuilding should be defined")
  }

  it should "not permit to assign a road if the spot is not empty" in {
    val state = ScatanState(threePlayers)
    val spot = spotToBuildRoad(state)
    val stateWithBuilding = for
      stateWithWood <- state.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      stateWithBrick <- stateWithWood.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      stateWithRoad <- stateWithBrick.build(spot, BuildingType.Road, threePlayers.head)
    yield stateWithRoad
    stateWithBuilding match
      case Some(state) =>
        state.assignedBuildings(spot) should be(AssignmentInfo(threePlayers.head, BuildingType.Road))
        state
          .assignBuilding(spot, BuildingType.Road, threePlayers.head) match
          case Some(_) => fail("state should not be defined")
          case None    => succeed

      case None => fail("stateWithBuilding should be defined")
  }

  it should "not allow to assign buildings if the player has't the necessary resources" in {
    val state = ScatanState(threePlayers)
    val stateWithNoBuildings = state.build(spotToBuildStructure(state), BuildingType.Settlement, threePlayers.head)
    stateWithNoBuildings should be(None)
  }

  it should "allow to assign buildings if the player has the necessary resources" in {
    val state = ScatanState(threePlayers)
    val spot = spotToBuildStructure(state)
    val stateWithBuilding = for
      stateWithWood <- state.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      stateWithBrick <- stateWithWood.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      stateWithSheep <- stateWithBrick.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Sheep))
      stateWithBuilding <- stateWithSheep.build(spot, BuildingType.Settlement, threePlayers.head)
    yield stateWithBuilding
    stateWithBuilding match
      case Some(state) =>
        state.assignedBuildings(spot) should be(AssignmentInfo(threePlayers.head, BuildingType.Settlement))
      case None => fail("stateWithBuilding should be defined")
  }

  it should "consume the resources when a building is assigned" in {
    val state = ScatanState(threePlayers)
    val stateWithBuilding = for
      stateWithWood <- state.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      stateWithBrick <- stateWithWood.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      stateWithSheep <- stateWithBrick.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Sheep))
      stateWithBuilding <- stateWithSheep.build(spotToBuildStructure(state), BuildingType.Settlement, threePlayers.head)
    yield stateWithBuilding
    stateWithBuilding match
      case Some(state) => state.resourceCards(threePlayers.head) should be(Seq.empty[ResourceCard])
      case None        => fail("stateWithBuilding should be defined")
  }

  it should "allow to place a City instead of a Settlement" in {
    val state = ScatanState(threePlayers)
    val spot = spotToBuildStructure(state)
    val stateWithBuilding = for
      stateWithBuilding <- state.assignBuilding(spot, BuildingType.Settlement, threePlayers.head)
      stateWithBuilding <- stateWithBuilding.assignBuilding(spot, BuildingType.City, threePlayers.head)
    yield stateWithBuilding
    stateWithBuilding match
      case Some(state) =>
        state.assignedBuildings(spot) should be(AssignmentInfo(threePlayers.head, BuildingType.City))
      case None => fail("stateWithBuilding should be defined")
  }

  it should "not allow to place a City if there is no Settlement in the spot" in {
    val state = ScatanState(threePlayers)
    val spot = spotToBuildStructure(state)
    val stateWithBuilding = for
      stateWithWood <- state.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      stateWithBrick <- stateWithWood.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      stateWithSheep <- stateWithBrick.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Sheep))
      stateWithWheat <- stateWithSheep.assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wheat))
      stateWithBuilding <- stateWithWheat.build(spot, BuildingType.City, threePlayers.head)
    yield stateWithBuilding
    stateWithBuilding should be(None)
  }
