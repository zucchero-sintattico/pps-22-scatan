package scatan.model.game.state

import scatan.model.map.Spot
import scatan.model.components.BuildingType
import scatan.lib.game.Player
import scatan.model.components.Cost

// trait BuildingCapacity extends BasicScatanState:
//   private def verifyResourceCost(player: Player, cost: Cost): Boolean =
//     cost.foldLeft(true)((result, resourceCost) =>
//       result && resourceCards(player).count(_.resourceType == resourceCost._1) >= resourceCost._2
//     )
//   def assignBuilding(position: Spot, buildingType: BuildingType, player: Player): BasicScatanState = ???
//   def build(position: Spot, buildingType: BuildingType, player: Player): BasicScatanState =
//     if verifyResourceCost(player, buildingType.cost) then
//       val remainingResourceCards = buildingType.cost.foldLeft(resourceCards(player))((cards, resourceCost) =>
//         cards.filter(_.resourceType != resourceCost._1).drop(resourceCost._2)
//       )
//       val gameWithConsumedResources =
//         BasicScatanState(resourceCards = resourceCards.updated(player, remainingResourceCards))
//       gameWithConsumedResources.assignBuilding(position, buildingType, player)
//     else this
