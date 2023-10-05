package scatan.model.game.ops

import scatan.model.components.*
import scatan.model.game.ops.BuildingOps.{assignBuilding, build}
import scatan.model.game.ops.CardOps.assignResourceCard
import scatan.model.game.ops.EmptySpotsOps.{emptyStructureSpot, emptyRoadSpot}
import scatan.model.game.BasicStateTest
import scatan.model.game.ScatanState
import scatan.model.map.StructureSpot
import scatan.model.map.RoadSpot

class BuildingOpsTest extends BasicStateTest:

  private def spotToBuildStructure(state: ScatanState): StructureSpot =
    state.emptyStructureSpot.head

  private def spotToBuildRoad(state: ScatanState): RoadSpot =
    state.emptyRoadSpot.head

  "A State with Buildings" should "have empty buildings when state start" in {
    val state = ScatanState(threePlayers)
    state.assignedBuildings.keySet should have size 0
  }

  it should "permit to assign a structure" in {
    val state = ScatanState(threePlayers)
    state
      .assignBuilding(spotToBuildStructure(state), BuildingType.Settlement, threePlayers.head)
      .get
      .assignedBuildings should have size 1
  }

  it should "permit to assign a road" in {
    val state = ScatanState(threePlayers)
    state
      .assignBuilding(spotToBuildRoad(state), BuildingType.Road, threePlayers.head)
      .get
      .assignedBuildings should have size 1
  }

  it should "not allow to assign buildings if the player has't the necessary resources" in {
    val state = ScatanState(threePlayers)
    val stateWithNoBuildings = state.build(spotToBuildStructure(state), BuildingType.Settlement, threePlayers.head)
    stateWithNoBuildings should be(None)
  }

  it should "allow to assign buildings if the player has the necessary resources" in {
    val state = ScatanState(threePlayers)
    val spot = spotToBuildStructure(state)
    val stateWithBuilding = state
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Sheep))
      .build(spot, BuildingType.Settlement, threePlayers.head)
    stateWithBuilding should not be None
    stateWithBuilding.get.assignedBuildings(spot) should be(
      AssignmentInfo(threePlayers.head, BuildingType.Settlement)
    )
  }

  it should "consume the resources when a building is assigned" in {
    val state = ScatanState(threePlayers)
    val stateWithBuilding = state
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Sheep))
      .build(spotToBuildStructure(state), BuildingType.Settlement, threePlayers.head)
    stateWithBuilding.get.resourceCards(threePlayers.head) should be(Seq.empty[ResourceCard])
  }
