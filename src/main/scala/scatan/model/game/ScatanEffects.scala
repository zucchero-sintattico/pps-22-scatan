package scatan.model.game

import scatan.lib.game.ops.Effect
import scatan.model.components.BuildingType
import scatan.model.game.config.ScatanActions.*
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.ops.BuildingOps.{assignBuilding, build}
import scatan.model.game.ops.CardOps.buyDevelopmentCard
import scatan.model.map.{Hexagon, RoadSpot, StructureSpot}

object ScatanEffects:

  def AssignSettlementEffect(player: ScatanPlayer, spot: StructureSpot): Effect[AssignSettlement.type, ScatanState] =
    (state: ScatanState) => state.assignBuilding(spot, BuildingType.Settlement, player)

  def AssignRoadEffect(player: ScatanPlayer, spot: RoadSpot): Effect[AssignRoad.type, ScatanState] =
    (state: ScatanState) => state.assignBuilding(spot, BuildingType.Road, player)

  def RollEffect(result: Int): Effect[RollDice.type, ScatanState] = (state: ScatanState) =>
    require(result != 7, "Use RollSevenEffect for rolling a 7")
    Some(state)

  def RollSevenEffect(): Effect[RollSeven.type, ScatanState] = (state: ScatanState) => Some(state)

  def PlaceRobberEffect(hex: Hexagon): Effect[PlaceRobber.type, ScatanState] = (state: ScatanState) => Some(state)

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

  def BuyDevelopmentCardEffect(player: ScatanPlayer): Effect[BuyDevelopmentCard.type, ScatanState] =
    (state: ScatanState) => state.buyDevelopmentCard(player)

  def PlayDevelopmentCardEffect(): Effect[PlayDevelopmentCard.type, ScatanState] = (state: ScatanState) => Some(state)

  def TradeWithBankEffect(): Effect[TradeWithBank.type, ScatanState] = (state: ScatanState) => Some(state)

  def TradeWithPlayerEffect(): Effect[TradeWithPlayer.type, ScatanState] = (state: ScatanState) => Some(state)
