package scatan.model.game

import scatan.model.components.ResourceCard

class StateWithResourceTest extends BasicStateTest:

  "A State with Resources" should "have an empty resource card deck initially" in {
    val state = ScatanState(threePlayers)
    state.resourceCards should be(ResourceCard.empty(threePlayers))
  }

  it should "assign a resource card after a dice roll" in {
    val state = ScatanState(threePlayers)
    val stateWithResources = state.assignResourcesFromNumber(6)
    stateWithResources.resourceCards should not be ResourceCard.empty(threePlayers)
  }
