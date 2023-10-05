package scatan.model.game.ops

import scatan.model.map.Hexagon
import scatan.model.game.ops.RobberOps.moveRobber
import scatan.model.game.BasicStateTest
import scatan.model.game.ScatanState

class RobberOpsTest extends BasicStateTest:

  "A State with Robber Ops" should "have a robber in the center of the map initially" in {
    val state = ScatanState(threePlayers)
    state.robberPlacement should be(Hexagon(0, 0, 0))
  }

  it should "move the robber to a new position" in {
    val state = ScatanState(threePlayers)
    val stateWithRobber = state.moveRobber(Hexagon(1, 0, -1))
    stateWithRobber match
      case Some(stateWithRobber) =>
        stateWithRobber.robberPlacement should be(Hexagon(1, 0, -1))
      case None => fail("Robber was not moved")
  }

  it should "not move the robber to the same position" in {
    val state = ScatanState(threePlayers)
    val stateWithRobber = state.moveRobber(Hexagon(0, 0, 0))
    stateWithRobber should be(None)
  }

  it should "not move the robber to a position outside the map" in {
    val state = ScatanState(threePlayers)
    val stateWithRobber = state.moveRobber(Hexagon(3, 0, -3))
    stateWithRobber should be(None)
  }
