package scatan.model.game.ops

import scatan.model.components.AssignmentInfo
import scatan.model.game.ScatanState
import scatan.model.game.config.ScatanPlayer
import scatan.model.map.HexagonInMap.layer
import scatan.model.map.{Hexagon, RoadSpot, StructureSpot}

object RobberOps:
  extension (state: ScatanState)
    /** Returns a new ScatanState with the robber moved to the specified hexagon. The robber can only be moved to a
      * hexagon with a terrain layer.
      *
      * @param hexagon
      *   the hexagon to move the robber to
      * @return
      *   Some(ScatanState) if the robber was moved, None otherwise
      */
    def moveRobber(hexagon: Hexagon): Option[ScatanState] =
      if state.robberPlacement == hexagon || hexagon.layer > state.gameMap.withTerrainLayers then None
      else Some(state.copy(robberPlacement = hexagon))

    /** Get the players that have buildings on the robber's current hexagon.
      * @return
      *   the players
      */
    def playersOnRobber: Seq[ScatanPlayer] =
      def isStructureSpotOverRobber(spot: StructureSpot): Boolean =
        spot.toSet.intersect(Set(state.robberPlacement)).nonEmpty
      state.assignedBuildings.collect {
        case (spot: StructureSpot, AssignmentInfo(player: ScatanPlayer, _)) if isStructureSpotOverRobber(spot) => player
      }.toSeq
