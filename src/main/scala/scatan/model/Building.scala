package scatan.model

import scatan.mvc.lib.Model

type ResourceCost = (ResourceType, Int)
type Cost = Map[ResourceType, Int]
object Cost:
  def apply(resourceCosts: ResourceCost*): Cost = resourceCosts.toMap

import BuildingType.*
enum BuildingType(val cost: Cost):
  case Settlement
      extends BuildingType(
        Cost(
          ResourceType.Wood * 1,
          ResourceType.Brick * 1,
          ResourceType.Wheat * 1,
          ResourceType.Sheep * 1
        )
      )
  case City extends BuildingType(Cost(ResourceType.Wheat * 2, ResourceType.Rock * 3))
  case Road extends BuildingType(Cost(ResourceType.Brick * 1, ResourceType.Wood * 1))

object BuildingType:
  extension (resourceType: ResourceType) def *(amount: Int): ResourceCost = (resourceType, amount)

final case class Building(buildingType: BuildingType)
