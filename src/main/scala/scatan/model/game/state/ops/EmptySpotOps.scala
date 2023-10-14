package scatan.model.game.state.ops

import scatan.model.game.state.ScatanState
import scatan.model.map.HexagonInMap.layer
import scatan.model.map.{RoadSpot, Spot, StructureSpot}

/** Operations on [[ScatanState]] related to empty spots.
  */
object EmptySpotOps:

  extension (state: ScatanState)

    /** Returns the empty spots of the game map.
      *
      * @return
      *   the empty spots of the game map
      */
    def emptySpots: Seq[Spot] =
      Seq(state.gameMap.nodes, state.gameMap.edges).flatten
        .filter(!state.assignedBuildings.isDefinedAt(_))

    /** Returns the empty structure spots of the game map. The empty structure spots are the spots where a Settlement or
      * a City can be built.
      * @return
      *   the empty structure spots of the game map
      */
    def emptyStructureSpots: Seq[StructureSpot] =
      state.gameMap.nodes
        .filter(!state.assignedBuildings.isDefinedAt(_))
        .filter(_.toSet.exists(_.layer <= state.gameMap.withTerrainLayers))
        .toSeq

    /** Returns the empty road spots of the game map. The empty road spots are the spots where a Road can be built.
      * @return
      *   the empty road spots of the game map
      */
    def emptyRoadSpots: Seq[RoadSpot] =
      state.gameMap.edges
        .filter(!state.assignedBuildings.isDefinedAt(_))
        .filter(_.toSet.forall(_.toSet.exists(_.layer <= state.gameMap.withTerrainLayers)))
        .toSeq
