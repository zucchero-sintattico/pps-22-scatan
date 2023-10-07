package scatan.model.game

import scatan.lib.game.ops.Effect
import scatan.model.components.BuildingType
import scatan.model.game.config.ScatanActions.*
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.ops.BuildingOps.{assignBuilding, build}
import scatan.model.map.{Hexagon, RoadSpot, StructureSpot}
import scatan.model.components.ResourceCard
import scatan.model.game.ops.CardOps.removeResourceCard
import scatan.model.game.ops.CardOps.assignResourceCard
import scatan.model.game.ops.TradeOps.tradeWithPlayer
import scatan.model.game.ops.CardOps.assignResourcesFromNumber
import scatan.model.game.ops.RobberOps.moveRobber

object ScatanEffects:

  def AssignSettlementEffect(player: ScatanPlayer, spot: StructureSpot): Effect[AssignSettlement.type, ScatanState] =
    (state: ScatanState) => state.assignBuilding(spot, BuildingType.Settlement, player)

  def AssignRoadEffect(player: ScatanPlayer, spot: RoadSpot): Effect[AssignRoad.type, ScatanState] =
    (state: ScatanState) => state.assignBuilding(spot, BuildingType.Road, player)

  def RollEffect(result: Int): Effect[RollDice.type, ScatanState] = (state: ScatanState) =>
    require(result != 7, "Use RollSevenEffect for rolling a 7")
    state.assignResourcesFromNumber(result)

  def RollSevenEffect(): Effect[RollSeven.type, ScatanState] = (state: ScatanState) => Some(state)

  def PlaceRobberEffect(hex: Hexagon): Effect[PlaceRobber.type, ScatanState] = (state: ScatanState) =>
    state.moveRobber(hex)

  def StoleCardEffect(player: ScatanPlayer): Effect[StoleCard.type, ScatanState] = (state: ScatanState) => Some(state)

  /*
   * Building Ops
   */

  def BuildRoadEffect(spot: RoadSpot, player: ScatanPlayer): Effect[BuildRoad.type, ScatanState] =
    (state: ScatanState) => state.build(spot, BuildingType.Road, player)

  def BuildSettlementEffect(spot: StructureSpot, player: ScatanPlayer): Effect[BuildSettlement.type, ScatanState] =
    (state: ScatanState) => state.build(spot, BuildingType.Settlement, player)

  def BuildCityEffect(spot: StructureSpot, player: ScatanPlayer): Effect[BuildCity.type, ScatanState] =
    (state: ScatanState) => state.build(spot, BuildingType.City, player)

  def BuyDevelopmentCardEffect(): Effect[BuyDevelopmentCard.type, ScatanState] = (state: ScatanState) => Some(state)

  def PlayDevelopmentCardEffect(): Effect[PlayDevelopmentCard.type, ScatanState] = (state: ScatanState) => Some(state)

  def TradeWithBankEffect(): Effect[TradeWithBank.type, ScatanState] = (state: ScatanState) => Some(state)

  def TradeWithPlayerEffect(
      sender: ScatanPlayer,
      receiver: ScatanPlayer,
      senderCards: Seq[ResourceCard],
      receiverCards: Seq[ResourceCard]
  ): Effect[TradeWithPlayer.type, ScatanState] =
    (state: ScatanState) => state.tradeWithPlayer(sender, receiver, senderCards, receiverCards)
