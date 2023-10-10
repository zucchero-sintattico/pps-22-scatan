package scatan.model.game

import scatan.lib.game.ops.Effect
import scatan.model.components.*
import scatan.model.game.config.ScatanActions.*
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.ops.BuildingOps.{assignBuilding, build}
import scatan.model.game.ops.CardOps.{assignResourceCard, assignResourcesFromNumber, buyDevelopmentCard, removeResourceCard}
import scatan.model.game.ops.RobberOps.moveRobber
import scatan.model.game.ops.TradeOps.{tradeWithBank, tradeWithPlayer}
import scatan.model.map.{Hexagon, RoadSpot, StructureSpot}

object ScatanEffects:

  private def EmptyEffect[A]: Effect[A, ScatanState] = (state: ScatanState) => Some(state)

  def NextTurnEffect(): Effect[NextTurn.type, ScatanState] = EmptyEffect

  def AssignSettlementEffect(player: ScatanPlayer, spot: StructureSpot): Effect[AssignSettlement.type, ScatanState] =
    (state: ScatanState) => state.assignBuilding(spot, BuildingType.Settlement, player)

  def AssignRoadEffect(player: ScatanPlayer, spot: RoadSpot): Effect[AssignRoad.type, ScatanState] =
    (state: ScatanState) => state.assignBuilding(spot, BuildingType.Road, player)

  def RollEffect(result: Int): Effect[RollDice.type, ScatanState] = (state: ScatanState) =>
    require(result != 7, "Use RollSevenEffect for rolling a 7")
    state.assignResourcesFromNumber(result)

  def RollSevenEffect(): Effect[RollSeven.type, ScatanState] = EmptyEffect

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

  def BuyDevelopmentCardEffect(player: ScatanPlayer, turnNumber: Int): Effect[BuyDevelopmentCard.type, ScatanState] =
    (state: ScatanState) => state.buyDevelopmentCard(player, turnNumber)

  def PlayDevelopmentCardEffect(): Effect[PlayDevelopmentCard.type, ScatanState] = EmptyEffect

  def TradeWithBankEffect(
      player: ScatanPlayer,
      offer: ResourceType,
      request: ResourceType
  ): Effect[TradeWithBank.type, ScatanState] = (state: ScatanState) =>
    state.tradeWithBank(
      player,
      offer,
      request
    )

  def TradeWithPlayerEffect(
      sender: ScatanPlayer,
      receiver: ScatanPlayer,
      senderCards: Seq[ResourceCard],
      receiverCards: Seq[ResourceCard]
  ): Effect[TradeWithPlayer.type, ScatanState] =
    (state: ScatanState) => state.tradeWithPlayer(sender, receiver, senderCards, receiverCards)
