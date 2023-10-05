package scatan.model.game

import scatan.BaseTest
import scatan.model.components.{Building, BuildingType, ResourceCard, ResourceType}

class StateWithBuildingsTest extends BasicStateTest:

  "A State with Buildings" should "have empty buildings when state start" in {
    val state = ScatanState(threePlayers)
    state.buildings(threePlayers.head) should be(Seq.empty[Building])
  }

  it should "not allow to assign buildings if the player has't the necessary resources" in {
    val state = ScatanState(threePlayers)
    val state2 = state.build(Building(BuildingType.Settlement), threePlayers.head)
    state.buildings(threePlayers.head) should be(Seq.empty[Building])
    state2.buildings(threePlayers.head) should be(Seq.empty[Building])
  }

  it should "allow to assign buildings if the player has the necessary resources" in {
    val state = ScatanState(threePlayers)
    val stateWithBuilding = state
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Sheep))
      .build(Building(BuildingType.Settlement), threePlayers.head)
    state.buildings(threePlayers.head) should be(Seq.empty[Building])
    stateWithBuilding.buildings(threePlayers.head) should be(Seq(Building(BuildingType.Settlement)))
  }

  it should "consume the resources when a building is assigned" in {
    val state = ScatanState(threePlayers)
    val stateWithBuilding = state
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Wood))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Brick))
      .assignResourceCard(threePlayers.head, ResourceCard(ResourceType.Sheep))
      .build(Building(BuildingType.Settlement), threePlayers.head)
    stateWithBuilding.resourceCards(threePlayers.head) should be(Seq.empty[ResourceCard])
  }
