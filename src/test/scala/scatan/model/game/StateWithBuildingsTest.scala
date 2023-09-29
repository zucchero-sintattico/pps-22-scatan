package scatan.model.game

import scatan.BaseTest
import scatan.model.components.{ResourceCard, ResourceType}
import scatan.model.components.{AssignedBuildings, BuildingType}
import scatan.model.game.ScatanState
import scatan.model.components.AssignedBuildingsAdapter.asPlayerMap
import scatan.model.components.AssignmentInfo

class StateWithBuildingsTest extends BasicStateTest:

  "A State with Buildings" should "have empty buildings when state start" in {
    val state = ScatanState(threePlayers)
    state.assignedBuildings.keySet should have size 0
  }

  it should "permit to assign a building" in {
    val state = ScatanState(threePlayers)
    state
      .assignBuilding(emptySpot(state), BuildingType.Settlement, threePlayers.head)
      .assignedBuildings should have size 1
  }

  it should "not allow to assign buildings if the player has't the necessary resources" in {
    val state = ScatanState(threePlayers)
    val state2 = state.build(emptySpot(state), BuildingType.Settlement, threePlayers.head)
    state2.assignedBuildings should have size 0
  }

  it should "allow to assign buildings if the player has the necessary resources" in {
    val state = ScatanState(threePlayers)
    val spotToBuild = emptySpot(state)
    val stateWithBuilding = state
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Sheep))
      .build(spotToBuild, BuildingType.Settlement, threePlayers.head)
    stateWithBuilding.assignedBuildings(spotToBuild) should be(
      AssignmentInfo(threePlayers.head, BuildingType.Settlement)
    )
  }

  it should "consume the resources when a building is assigned" in {
    val state = ScatanState(threePlayers)
    val stateWithBuilding = state
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Sheep))
      .build(emptySpot(state), BuildingType.Settlement, threePlayers.head)
    stateWithBuilding.resourceCards(threePlayers.head) should be(Seq.empty[ResourceCard])
  }
