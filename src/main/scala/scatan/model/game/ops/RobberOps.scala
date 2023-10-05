package scatan.model.game.ops

import scatan.model.game.ScatanState
import scatan.model.map.Hexagon

object RobberOps:
  extension (state: ScatanState)
    /** Returns a new ScatanState with the robber moved to the specified hexagon.
      *
      * @param hexagon
      *   the hexagon to move the robber to
      * @return
      *   a new ScatanState with the robber moved to the specified hexagon
      */
    def moveRobber(hexagon: Hexagon): ScatanState = state.copy(robberPlacement = hexagon)
