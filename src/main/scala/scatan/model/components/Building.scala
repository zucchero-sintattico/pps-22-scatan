package scatan.model.components

import scatan.lib.game.Player
import scatan.model.components.*
import scatan.model.components.BuildingType.*
import scatan.model.map.Spot
import ResourceType.*
import scatan.model.map.StructureSpot
import scatan.model.map.RoadSpot

type ResourceCost = (ResourceType, Int)
type Cost = Map[ResourceType, Int]

object Cost:
  def apply(resourceCosts: ResourceCost*): Cost = resourceCosts.toMap

enum BuildingType(val cost: Cost):
  case Settlement
      extends BuildingType(
        Cost(
          Wood * 1,
          Brick * 1,
          Sheep * 1
        )
      )
  case City
      extends BuildingType(
        Cost(
          Wheat * 2,
          Rock * 3
        )
      )
  case Road
      extends BuildingType(
        Cost(
          Wood * 1,
          Brick * 1
        )
      )

object BuildingType:
  extension (resourceType: ResourceType) def *(amount: Int): ResourceCost = (resourceType, amount)

/** A building is a structure that can be placed on the map.
  */
trait AssignmentInfo:
  def player: Player
  def buildingType: BuildingType

object AssignmentInfo:
  def apply(player: Player, buildingType: BuildingType): AssignmentInfo = AssignmentInfoImpl(player, buildingType)
  private case class AssignmentInfoImpl(player: Player, buildingType: BuildingType) extends AssignmentInfo

/** A map of assigned buildings.
  */
type AssignedBuildings = Map[Spot, AssignmentInfo]

object AssignmentFactory:
  def apply(spot: Spot, player: Player, buildingType: BuildingType): (Spot, AssignmentInfo) =
    spot -> AssignmentInfo(player, buildingType)

object AssignedBuildingsAdapter:

  /** An adapter to convert a map of assigned buildings to a map of players and their buildings.
    */
  extension (assignedBuildings: AssignedBuildings)
    def asPlayerMap: Map[Player, Seq[BuildingType]] =
      assignedBuildings.foldLeft(Map.empty[Player, Seq[BuildingType]])((playerMap, assignment) =>
        playerMap.updated(
          assignment._2.player,
          playerMap.getOrElse(assignment._2.player, Seq.empty[BuildingType]) :+ assignment._2.buildingType
        )
      )
    def getStructureSpots(): Map[StructureSpot, AssignmentInfo] =
      assignedBuildings.filter(_._1.isInstanceOf[StructureSpot]).asInstanceOf[Map[StructureSpot, AssignmentInfo]]
    def getRoadSpots(): Seq[RoadSpot] =
      assignedBuildings.keys.toSeq.collect { case spot: RoadSpot => spot }
