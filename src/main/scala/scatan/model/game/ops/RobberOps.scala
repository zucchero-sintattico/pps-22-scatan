package scatan.model.game.ops

import scatan.model.game.ScatanState
import scatan.model.map.Hexagon
import scatan.model.map.HexagonInMap.layer

object RobberOps:
  extension (state: ScatanState)
    /** Returns a new ScatanState with the robber moved to the specified hexagon.
      *
      * @param hexagon
      *   the hexagon to move the robber to
      * @return
      *   Some(ScatanState) if the robber was moved, None otherwise
      */
    def moveRobber(hexagon: Hexagon): Option[ScatanState] =
      if state.robberPlacement == hexagon || hexagon.layer > state.gameMap.withTerrainLayers then None
      else Some(state.copy(robberPlacement = hexagon))
