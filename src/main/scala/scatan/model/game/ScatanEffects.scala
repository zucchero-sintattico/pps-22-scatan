package scatan.model.game

import scatan.lib.game.ops.Effect
import scatan.model.components.{BuildingType, ResourceCard, ResourceType}
import scatan.model.game.config.ScatanActions.*
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.state.ScatanState
import scatan.model.game.state.ops.BuildingOps.{assignBuilding, build}
import scatan.model.game.state.ops.DevelopmentCardOps.*
import scatan.model.game.state.ops.ResourceCardOps.*
import scatan.model.game.state.ops.RobberOps.moveRobber
import scatan.model.game.state.ops.TradeOps.{tradeWithBank, tradeWithPlayer}
import scatan.model.map.{Hexagon, RoadSpot, StructureSpot}

object ScatanEffects:

  /** An effect that does nothing
    * @tparam A
    *   The type of the action
    * @return
    *   An effect that does nothing
    */
  def EmptyEffect[A]: Effect[A, ScatanState] = (state: ScatanState) => Some(state)

  /** Changes the current player to the next player. It's an empty effect because the player is changed in the game
    * engine.
    */
  def NextTurnEffect(): Effect[NextTurn.type, ScatanState] = EmptyEffect

  /** Assigns a building to a spot
    * @param player
    *   The player who is building
    * @param spot
    *   The spot to build on
    * @return
    *   An effect that assigns a building to a spot
    */
  def AssignSettlementEffect(player: ScatanPlayer, spot: StructureSpot): Effect[AssignSettlement.type, ScatanState] =
    (state: ScatanState) => state.assignBuilding(spot, BuildingType.Settlement, player)

  /** Assigns a road to a spot
    * @param player
    *   The player who is building
    * @param spot
    *   The spot to build on
    * @return
    *   An effect that assigns a road to a spot
    */
  def AssignRoadEffect(player: ScatanPlayer, spot: RoadSpot): Effect[AssignRoad.type, ScatanState] =
    (state: ScatanState) => state.assignBuilding(spot, BuildingType.Road, player)

  /** Roll the dice and assign resources
    * @param result
    *   The result of the dice roll
    * @return
    *   An effect that rolls the dice and assigns resources
    */
  def RollEffect(result: Int): Effect[RollDice.type, ScatanState] = (state: ScatanState) =>
    require(result != 7, "Use RollSevenEffect for rolling a 7")
    state.assignResourcesFromNumber(result)

  /** Roll a 7
    * @return
    *   An effect that rolls a 7
    */
  def RollSevenEffect(): Effect[RollSeven.type, ScatanState] = EmptyEffect

  /** Move the robber
    * @param hex
    *   The hexagon to move the robber to
    * @return
    *   An effect that moves the robber
    */
  def PlaceRobberEffect(hex: Hexagon): Effect[PlaceRobber.type, ScatanState] = (state: ScatanState) =>
    state.moveRobber(hex)

  /** Steal a card from a player
    * @param currentPlayer
    *   The player who is stealing
    * @param victim
    *   The player who is being stolen from
    * @return
    *   An effect that steals a card from a player
    */
  def StealCardEffect(currentPlayer: ScatanPlayer, victim: ScatanPlayer): Effect[StealCard.type, ScatanState] =
    (state: ScatanState) => state.stoleResourceCard(currentPlayer, victim)

  /*
   * Building Ops
   */

  /** Build a road
    * @param spot
    *   The spot to build on
    * @param player
    *   The player who is building
    * @return
    *   An effect that builds a road
    */
  def BuildRoadEffect(spot: RoadSpot, player: ScatanPlayer): Effect[BuildRoad.type, ScatanState] =
    (state: ScatanState) => state.build(spot, BuildingType.Road, player)

  /** Build a settlement
    * @param spot
    *   The spot to build on
    * @param player
    *   The player who is building
    * @return
    *   An effect that builds a settlement
    */
  def BuildSettlementEffect(spot: StructureSpot, player: ScatanPlayer): Effect[BuildSettlement.type, ScatanState] =
    (state: ScatanState) => state.build(spot, BuildingType.Settlement, player)

  /** Build a city
    * @param spot
    *   The spot to build on
    * @param player
    *   The player who is building
    * @return
    *   An effect that builds a city
    */
  def BuildCityEffect(spot: StructureSpot, player: ScatanPlayer): Effect[BuildCity.type, ScatanState] =
    (state: ScatanState) => state.build(spot, BuildingType.City, player)

  /** Buy a development card
    * @param player
    *   The player who is buying
    * @param turnNumber
    *   The turn number
    * @return
    *   An effect that buys a development card
    */
  def BuyDevelopmentCardEffect(player: ScatanPlayer, turnNumber: Int): Effect[BuyDevelopmentCard.type, ScatanState] =
    (state: ScatanState) => state.buyDevelopmentCard(player, turnNumber)

  /*
   * Development Card Ops
   */

  /** Play a knight development card
    * @param player
    *   The player who is playing the card
    * @param turnNumber
    *   The turn number
    * @param robberPosition
    *   The position to move the robber to
    * @return
    *   An effect that plays a knight development card
    */
  def PlayKnightDevelopmentCardEffect(
      player: ScatanPlayer,
      turnNumber: Int,
      robberPosition: Hexagon
  ): Effect[PlayDevelopmentCard.type, ScatanState] =
    (state: ScatanState) => state.playKnightDevelopment(player, robberPosition, turnNumber)

  /** Play a monopoly development card
    * @param player
    *   The player who is playing the card
    * @param turnNumber
    *   The turn number
    * @param resourceType
    *   The resource type to monopolize
    * @return
    *   An effect that plays a monopoly development card
    */
  def PlayMonopolyDevelopmentCardEffect(
      player: ScatanPlayer,
      turnNumber: Int,
      resourceType: ResourceType
  ): Effect[PlayDevelopmentCard.type, ScatanState] =
    (state: ScatanState) => state.playMonopolyDevelopment(player, resourceType, turnNumber)

  /** Play a year of plenty development card
    * @param player
    *   The player who is playing the card
    * @param turnNumber
    *   The turn number
    * @param firstResourceType
    *   The first resource type to get
    * @param secondResourceType
    *   The second resource type to get
    * @return
    *   An effect that plays a year of plenty development card
    */
  def PlayYearOfPlentyDevelopmentCardEffect(
      player: ScatanPlayer,
      turnNumber: Int,
      firstResourceType: ResourceType,
      secondResourceType: ResourceType
  ): Effect[PlayDevelopmentCard.type, ScatanState] =
    (state: ScatanState) => state.playYearOfPlentyDevelopment(player, firstResourceType, secondResourceType, turnNumber)

  /** Play a road building development card
    * @param player
    *   The player who is playing the card
    * @param turnNumber
    *   The turn number
    * @param spot1
    *   The first road spot to build on
    * @param spot2
    *   The second road spot to build on
    * @return
    *   An effect that plays a road building development card
    */
  def PlayRoadBuildingDevelopmentCardEffect(
      player: ScatanPlayer,
      turnNumber: Int,
      spot1: RoadSpot,
      spot2: RoadSpot
  ): Effect[PlayDevelopmentCard.type, ScatanState] =
    (state: ScatanState) => state.playRoadBuildingDevelopment(player, spot1, spot2, turnNumber)

  /** Play a victory point development card
    * @param player
    *   The player who is playing the card
    * @param offer
    *   The resource type to offer
    * @param request
    *   The resource type to request
    * @return
    *   An effect that plays a victory point development card
    */
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

  /** Trade with a player
    * @param sender
    *   The player who is sending the trade
    * @param receiver
    *   The player who is receiving the trade
    * @param senderCards
    *   The cards the sender is offering
    * @param receiverCards
    *   The cards the receiver is offering
    * @return
    */
  def TradeWithPlayerEffect(
      sender: ScatanPlayer,
      receiver: ScatanPlayer,
      senderCards: Seq[ResourceCard],
      receiverCards: Seq[ResourceCard]
  ): Effect[TradeWithPlayer.type, ScatanState] =
    (state: ScatanState) => state.tradeWithPlayer(sender, receiver, senderCards, receiverCards)
