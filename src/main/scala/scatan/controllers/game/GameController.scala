package scatan.controllers.game

import scatan.lib.mvc.{BaseController, Controller}
import scatan.model.ApplicationState
import scatan.model.components.*
import scatan.model.game.ScatanModelOps.{onError, updateGame}
import scatan.model.game.config.ScatanPlayer
import scatan.model.map.{Hexagon, RoadSpot, StructureSpot}
import scatan.views.game.GameView

/** The controller for the game.
  */
trait GameController extends Controller[ApplicationState]:
  def state: ApplicationState

  /** Goes to the next turn. */
  def nextTurn(): Unit

  /** Rolls the dice. */
  def rollDice(): Unit

  /** Assigns a road to the current player.
    * @param spot
    *   The spot to assign the road to.
    */
  def assignRoad(spot: RoadSpot): Unit

  /** Assigns a settlement to the current player.
    * @param spot
    *   The spot to assign the settlement to.
    */
  def assignSettlement(spot: StructureSpot): Unit

  /** Builds a road for the current player.
    * @param spot
    *   The spot to build the road on.
    */
  def buildRoad(spot: RoadSpot): Unit

  /** Builds a settlement for the current player.
    * @param spot
    *   The spot to build the settlement on.
    */
  def buildSettlement(spot: StructureSpot): Unit

  /** Builds a city for the current player.
    * @param spot
    *   The spot to build the city on.
    */
  def buildCity(spot: StructureSpot): Unit

  /** Places the robber on a hexagon.
    * @param hexagon
    *   The hexagon to place the robber on.
    */
  def placeRobber(hexagon: Hexagon): Unit

  /** Steals a card from a player.
    * @param player
    *   The player to steal a card from.
    */
  def stealCard(player: ScatanPlayer): Unit

  /** Buys a development card for the current player.
    */
  def buyDevelopmentCard(): Unit

  /** Plays a knight development card for the current player.
    * @param robberPosition
    *   The hexagon to place the robber on.
    */
  def playKnightDevelopment(robberPosition: Hexagon): Unit

  /** Plays a year of plenty development card for the current player.
    * @param resource1
    *   The first resource to get.
    * @param resource2
    *   The second resource to get.
    */
  def playYearOfPlentyDevelopment(resource1: ResourceType, resource2: ResourceType): Unit

  /** Plays a monopoly development card for the current player.
    * @param resource
    *   The resource to get.
    */
  def playMonopolyDevelopment(resource: ResourceType): Unit

  /** Plays a road building development card for the current player.
    * @param spot1
    *   The first spot to build a road on.
    * @param spot2
    *   The second spot to build a road on.
    */
  def playRoadBuildingDevelopment(spot1: RoadSpot, spot2: RoadSpot): Unit

  /** Trades with the bank.
    * @param offer
    *   The resource to offer.
    * @param request
    *   The resource to request.
    */
  def tradeWithBank(offer: ResourceType, request: ResourceType): Unit

  /** Trades with a player.
    * @param receiver
    *   The player to trade with.
    * @param offer
    *   The cards to offer.
    * @param request
    *   The cards to request.
    */
  def tradeWithPlayer(receiver: ScatanPlayer, offer: Seq[ResourceCard], request: Seq[ResourceCard]): Unit

object GameController:
  def apply(requirements: Controller.Requirements[GameView, ApplicationState]): GameController =
    GameControllerImpl(requirements)

private class GameControllerImpl(requirements: Controller.Requirements[GameView, ApplicationState])
    extends BaseController(requirements)
    with GameController:

  override def state: ApplicationState = this.model.state

  override def assignRoad(spot: RoadSpot): Unit =
    this.model
      .updateGame(_.assignRoad(spot))
      .onError(view.displayMessage("Cannot assign road here"))

  override def assignSettlement(spot: StructureSpot): Unit =
    this.model
      .updateGame(_.assignSettlement(spot))
      .onError(view.displayMessage("Cannot assign settlement here"))

  override def buildRoad(spot: RoadSpot): Unit =
    this.model
      .updateGame(_.buildRoad(spot))
      .onError(view.displayMessage("Cannot build road here"))

  override def buildSettlement(spot: StructureSpot): Unit =
    this.model
      .updateGame(_.buildSettlement(spot))
      .onError(view.displayMessage("Cannot build settlement here"))

  override def buildCity(spot: StructureSpot): Unit =
    this.model
      .updateGame(_.buildCity(spot))
      .onError(view.displayMessage("Cannot build city here"))

  override def placeRobber(hexagon: Hexagon): Unit =
    this.model
      .updateGame(_.placeRobber(hexagon))
      .onError(view.displayMessage("Cannot place robber"))

  override def nextTurn(): Unit =
    this.model
      .updateGame(_.nextTurn)
      .onError(view.displayMessage("Cannot go to next turn"))

  override def rollDice(): Unit =
    this.model
      .updateGame(_.rollDice(diceResult => this.view.displayMessage(s"Roll dice result: $diceResult")))
      .onError(view.displayMessage("Cannot roll dice"))

  override def stealCard(player: ScatanPlayer): Unit =
    this.model
      .updateGame(_.stealCard(player))
      .onError(view.displayMessage("Cannot steal card"))

  override def buyDevelopmentCard(): Unit =
    this.model
      .updateGame(_.buyDevelopmentCard)
      .onError(view.displayMessage("Cannot buy development card"))

  override def playKnightDevelopment(robberPosition: Hexagon): Unit =
    this.model
      .updateGame(_.playKnightDevelopment(robberPosition))
      .onError(view.displayMessage("Cannot play knight development card"))

  override def playYearOfPlentyDevelopment(resource1: ResourceType, resource2: ResourceType): Unit =
    this.model
      .updateGame(_.playYearOfPlentyDevelopment(resource1, resource2))
      .onError(view.displayMessage("Cannot play year of plenty development card"))

  override def playMonopolyDevelopment(resource: ResourceType): Unit =
    this.model
      .updateGame(_.playMonopolyDevelopment(resource))
      .onError(view.displayMessage("Cannot play monopoly development card"))

  override def playRoadBuildingDevelopment(spot1: RoadSpot, spot2: RoadSpot): Unit =
    this.model
      .updateGame(_.playRoadBuildingDevelopment(spot1, spot2))
      .onError(view.displayMessage("Cannot play road building development card"))

  override def tradeWithBank(offer: ResourceType, request: ResourceType): Unit =
    this.model
      .updateGame(_.tradeWithBank(offer, request))
      .onError(view.displayMessage("Cannot trade with bank"))

  override def tradeWithPlayer(receiver: ScatanPlayer, offer: Seq[ResourceCard], request: Seq[ResourceCard]): Unit =
    this.model
      .updateGame(_.tradeWithPlayer(receiver, offer, request))
      .onError(view.displayMessage("Cannot trade with player"))
