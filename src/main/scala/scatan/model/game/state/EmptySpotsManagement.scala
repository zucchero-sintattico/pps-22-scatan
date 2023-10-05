package scatan.model.game.state

import scatan.model.map.Spot
import scatan.model.map.StructureSpot
import scatan.model.map.RoadSpot
import scatan.model.map.HexagonInMap.layer

trait EmptySpotsManagement[S <: EmptySpotsManagement[S]] extends BasicScatanState[S]:

  def emptySpots: Seq[Spot] =
    Seq(gameMap.nodes, gameMap.edges).flatten
      .filter(!assignedBuildings.isDefinedAt(_))

  def emptyStructureSpot: Seq[StructureSpot] =
    gameMap.nodes
      .filter(!assignedBuildings.isDefinedAt(_))
      .filter(_.toSet.exists(_.layer <= gameMap.withTerrainLayers))
      .toSeq

  def emptyRoadSpot: Seq[RoadSpot] =
    gameMap.edges
      .filter(!assignedBuildings.isDefinedAt(_))
      .filter(_.toSet.exists(_.toSet.exists(_.layer <= gameMap.withTerrainLayers)))
      .toSeq
