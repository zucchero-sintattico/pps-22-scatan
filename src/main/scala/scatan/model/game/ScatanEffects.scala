package scatan.model.game

import scatan.lib.game.ops.Effect
import scatan.model.components.{BuildingType, DevelopmentType, ResourceCard, ResourceType}
import scatan.model.game.config.ScatanActions.*
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.ops.BuildingOps.{assignBuilding, build}
import scatan.model.game.ops.CardOps.{
  assignResourceCard,
  assignResourcesFromNumber,
  buyDevelopmentCard,
  playKnightDevelopment,
  playMonopolyDevelopment,
  playRoadBuildingDevelopment,
  playYearOfPlentyDevelopment,
  removeResourceCard,
  stoleResourceCard
}
import scatan.model.map.{Hexagon, RoadSpot, StructureSpot}
import scatan.model.components.ResourceCard
import scatan.model.game.ops.TradeOps.tradeWithPlayer
import scatan.model.game.ops.RobberOps.moveRobber
import scatan.model.game.ops.TradeOps.tradeWithPlayer
import scatan.model.map.{Hexagon, RoadSpot, StructureSpot}

object ScatanEffects:

  def EmptyEffect[A]: Effect[A, ScatanState] = (state: ScatanState) => Some(state)

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

  def StealCardEffect(currentPlayer: ScatanPlayer, victim: ScatanPlayer): Effect[StealCard.type, ScatanState] =
    (state: ScatanState) => state.stoleResourceCard(currentPlayer, victim)

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

  /*
   * Development Card Ops
   */

  def PlayKnightDevelopmentCardEffect(
      player: ScatanPlayer,
      turnNumber: Int,
      robberPosition: Hexagon
  ): Effect[PlayDevelopmentCard.type, ScatanState] =
    (state: ScatanState) => state.playKnightDevelopment(player, robberPosition, turnNumber)

  def PlayMonopolyDevelopmentCardEffect(
      player: ScatanPlayer,
      turnNumber: Int,
      resourceType: ResourceType
  ): Effect[PlayDevelopmentCard.type, ScatanState] =
    (state: ScatanState) => state.playMonopolyDevelopment(player, resourceType, turnNumber)

  def PlayYearOfPlentyDevelopmentCardEffect(
      player: ScatanPlayer,
      turnNumber: Int,
      firstResourceType: ResourceType,
      secondResourceType: ResourceType
  ): Effect[PlayDevelopmentCard.type, ScatanState] =
    (state: ScatanState) => state.playYearOfPlentyDevelopment(player, firstResourceType, secondResourceType, turnNumber)

  def PlayRoadBuildingDevelopmentCardEffect(
      player: ScatanPlayer,
      turnNumber: Int,
      spot1: RoadSpot,
      spot2: RoadSpot
  ): Effect[PlayDevelopmentCard.type, ScatanState] =
    (state: ScatanState) => state.playRoadBuildingDevelopment(player, spot1, spot2, turnNumber)

  def TradeWithBankEffect(): Effect[TradeWithBank.type, ScatanState] = EmptyEffect

  def TradeWithPlayerEffect(
      sender: ScatanPlayer,
      receiver: ScatanPlayer,
      senderCards: Seq[ResourceCard],
      receiverCards: Seq[ResourceCard]
  ): Effect[TradeWithPlayer.type, ScatanState] =
    (state: ScatanState) => state.tradeWithPlayer(sender, receiver, senderCards, receiverCards)
