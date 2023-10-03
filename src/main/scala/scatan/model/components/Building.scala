package scatan.model.components

import scatan.model.components.*
import scatan.model.components.BuildingType.*
import scatan.model.components.ResourceType.*
import scatan.model.game.ScatanPlayer

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

final case class Building(buildingType: BuildingType)

/** A map of players to their buildings
  */
type Buildings = Map[ScatanPlayer, Seq[Building]]
object Building:
  /** Returns a map of players to an empty buildings sequence
    *
    * @param players
    *   the players to create the empty buildings map for
    * @return
    *   the empty buildings map
    */
  def empty(players: Seq[ScatanPlayer]): Buildings =
    players.map(player => (player, Seq.empty[Building])).toMap
