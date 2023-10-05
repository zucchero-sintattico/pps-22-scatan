package scatan.model.game.state

import scatan.model.map.Spot
import scatan.lib.game.Player
import scatan.model.components.BuildingType
import scatan.model.map.RoadSpot
import scatan.model.components.AssignmentInfo
import scatan.model.map.StructureSpot

trait BuildingCapacity[S <: BuildingCapacity[S]] extends BasicScatanState[S]:
  def build(position: Spot, buildingType: BuildingType, player: Player): S
  def assignBuilding(spot: Spot, buildingType: BuildingType, player: Player): S =
    val buildingUpdated =
      spot match
        case s: RoadSpot if emptyRoadSpot.contains(s) =>
          assignedBuildings.updated(s, AssignmentInfo(player, buildingType))
        case s: StructureSpot if emptyStructureSpot.contains(s) =>
          assignedBuildings.updated(s, AssignmentInfo(player, buildingType))
        case _ => assignedBuildings
    ???
