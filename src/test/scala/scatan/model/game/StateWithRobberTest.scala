package scatan.model.game

import scatan.model.map.Hexagon

class StateWithRobberTest extends BasicStateTest:

  "A State with Robber" should "have a robber in the center of the map initially" in {
    val state = ScatanState(threePlayers)
    state.robberPlacement should be(Hexagon(0, 0, 0))
  }

  it should "move the robber to a new position" in {
    val state = ScatanState(threePlayers)
    val stateWithRobber = state.moveRobber(Hexagon(1, 0, -1))
    stateWithRobber.robberPlacement should be(Hexagon(1, 0, -1))
  }
