package scatan.model

import scatan.lib.mvc.Model
import ResourceType.*

type ResourceCost = (ResourceType, Int)
type Cost = Map[ResourceType, Int]

object Cost:
  def apply(resourceCosts: ResourceCost*): Cost = resourceCosts.toMap

import BuildingType.*
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

final case class Building(buildingType: BuildingType)

type Buildings = Map[Player, Seq[Building]]
object Building:
  def empty(players: Seq[Player]): Buildings =
    // map each player to a sequence of 0 buildings
    players.map(player => (player, Seq.empty[Building])).toMap
