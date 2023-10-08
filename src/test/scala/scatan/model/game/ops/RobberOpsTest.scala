package scatan.model.game.ops

import scatan.model.map.Hexagon
import scatan.model.game.ops.RobberOps.moveRobber
import scatan.model.game.BaseScatanStateTest
import scatan.model.game.ScatanState
import scatan.model.components.UnproductiveTerrain.Desert

class RobberOpsTest extends BaseScatanStateTest:

  "A State with Robber Ops" should "have a robber over the desert initially" in {
    val state = ScatanState(threePlayers)
    val desertHexagon = state.gameMap.tiles.find(state.gameMap.toContent(_).terrain == Desert).get
    state.robberPlacement should be(desertHexagon)
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
    val stateWithRobber = state.moveRobber(state.robberPlacement)
    stateWithRobber should be(None)
  }

  it should "not move the robber to a position outside the map" in {
    val state = ScatanState(threePlayers)
    val stateWithRobber = state.moveRobber(Hexagon(3, 0, -3))
    stateWithRobber should be(None)
  }
