package scatan.model.game

import scatan.lib.game.ops.Effect
import scatan.model.components.BuildingType
import scatan.model.game.config.ScatanActions.*
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.ops.BuildingOps.assignBuilding
import scatan.model.map.{Hexagon, StructureSpot, RoadSpot}

object ScatanEffects:

  def AssignSettlementEffect(player: ScatanPlayer, spot: StructureSpot): Effect[AssignSettlement.type, ScatanState] =
    (state: ScatanState) => Some(state.assignBuilding(spot, BuildingType.Settlement, player))

  def AssignRoadEffect(player: ScatanPlayer, spot: RoadSpot): Effect[AssignRoad.type, ScatanState] =
    (state: ScatanState) => Some(state.assignBuilding(spot, BuildingType.Road, player))

  def RollEffect(result: Int): Effect[RollDice.type, ScatanState] = (state: ScatanState) =>
    require(result != 7, "Use RollSevenEffect for rolling a 7")
    Some(state)

  def RollSevenEffect(): Effect[RollSeven.type, ScatanState] = (state: ScatanState) => Some(state)

  def PlaceRobberEffect(hex: Hexagon): Effect[PlaceRobber.type, ScatanState] = (state: ScatanState) => Some(state)

  def StoleCardEffect(player: ScatanPlayer): Effect[StoleCard.type, ScatanState] = (state: ScatanState) => Some(state)

  def BuildRoadEffect(): Effect[BuildRoad.type, ScatanState] = (state: ScatanState) => Some(state)

  def BuildSettlementEffect(spot: StructureSpot, player: ScatanPlayer): Effect[BuildSettlement.type, ScatanState] =
    (state: ScatanState) => Some(state.assignBuilding(spot, BuildingType.Settlement, player))

  def BuildCityEffect(): Effect[BuildCity.type, ScatanState] = (state: ScatanState) => Some(state)

  def BuyDevelopmentCardEffect(): Effect[BuyDevelopmentCard.type, ScatanState] = (state: ScatanState) => Some(state)

  def PlayDevelopmentCardEffect(): Effect[PlayDevelopmentCard.type, ScatanState] = (state: ScatanState) => Some(state)

  def TradeWithBankEffect(): Effect[TradeWithBank.type, ScatanState] = (state: ScatanState) => Some(state)

  def TradeWithPlayerEffect(): Effect[TradeWithPlayer.type, ScatanState] = (state: ScatanState) => Some(state)
