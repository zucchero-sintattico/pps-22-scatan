package scatan.model.game.ops

import scatan.model.game.ScatanState
import scatan.model.map.HexagonInMap.layer
import scatan.model.map.{RoadSpot, Spot, StructureSpot}

object EmptySpotsOps:

  extension (state: ScatanState)
    def emptySpots: Seq[Spot] =
      Seq(state.gameMap.nodes, state.gameMap.edges).flatten
        .filter(!state.assignedBuildings.isDefinedAt(_))

    def emptyStructureSpot: Seq[StructureSpot] =
      state.gameMap.nodes
        .filter(!state.assignedBuildings.isDefinedAt(_))
        .filter(_.toSet.exists(_.layer <= state.gameMap.withTerrainLayers))
        .toSeq

    def emptyRoadSpot: Seq[RoadSpot] =
      state.gameMap.edges
        .filter(!state.assignedBuildings.isDefinedAt(_))
        .filter(_.toSet.exists(_.toSet.exists(_.layer <= state.gameMap.withTerrainLayers)))
        .toSeq