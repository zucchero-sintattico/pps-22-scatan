package scatan.model.game

import scatan.lib.game.ops.Effect
import scatan.lib.game.ops.GamePlayOps.{allowedActions, play}
import scatan.lib.game.ops.GameWinOps.{isOver, winner}
import scatan.lib.game.{Game, GameStatus, Turn}
import scatan.model.map.GameMap
import scatan.model.components.ResourceType.{Rock, Sheep, Wheat}
import scatan.model.components.{DevelopmentType, ResourceCard, ResourceType}
import scatan.model.game.ScatanEffects.*
import scatan.model.game.config.ScatanActions.*
import scatan.model.game.config.{ScatanActions, ScatanPhases, ScatanPlayer, ScatanSteps}
import scatan.model.game.state.ScatanState
import scatan.model.game.state.ops.RobberOps.playersOnRobber
import scatan.model.map.{Hexagon, RoadSpot, StructureSpot}

import scala.util.Random

/** The status of a game of Scatan. It contains all the data without any possible action.
  * @param game
  *   The internal game
  */
private trait ScatanGameStatus(
    protected val game: Game[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer]
):
  def players: Seq[ScatanPlayer] = game.players
  def state: ScatanState = game.state
  def turn: Turn[ScatanPlayer] = game.turn
  def gameStatus: GameStatus[ScatanPhases, ScatanSteps] = game.gameStatus
  def isOver: Boolean = game.isOver
  def winner: Option[ScatanPlayer] = game.winner
  def allowedActions: Set[ScatanActions] = game.allowedActions.filter(_ != RollSeven)
  def playersOnRobber: Seq[ScatanPlayer] = game.state.playersOnRobber

private trait ScatanGameActions extends ScatanGameStatus:

  private def play(action: ScatanActions)(using effect: Effect[action.type, ScatanState]): Option[ScatanGame] =
    game.play(action).map(ScatanGame.apply)

  /** Checks if the player can build a road.
    * @return
    *   true if the player can build a road, false otherwise
    */
  def canBuyDevelopment: Boolean =
    val required = Seq(Rock, Wheat, Sheep)
    game.allowedActions.contains(BuyDevelopmentCard) && required.forall(resource =>
      game.state.resourceCards(game.turn.player).exists(_.resourceType == resource)
    )

  /** Checks if the player can play the given development.
    * @param developmentType
    *   the development type
    * @return
    *   true if the player can build a road, false otherwise
    */
  def canPlayDevelopment(developmentType: DevelopmentType): Boolean =
    game.allowedActions.contains(PlayDevelopmentCard) && game.state
      .developmentCards(game.turn.player)
      .exists(card => card.developmentType == developmentType && !card.played && card.drewAt.get < game.turn.number)

  /** Changes the turn to the next player.
    * @return
    *   the new game, if the action is allowed
    */
  def nextTurn: Option[ScatanGame] =
    play(ScatanActions.NextTurn)(using NextTurnEffect())

  /*
   * Assign Ops
   */

  /** Assigns a settlement to the given spot.
    * @param spot
    *   the spot
    * @return
    *   the new game, if the action is allowed
    */
  def assignSettlement(spot: StructureSpot): Option[ScatanGame] =
    play(AssignSettlement)(using AssignSettlementEffect(game.turn.player, spot))

  /** Assigns a city to the given spot.
    * @param road
    *   the spot
    * @return
    *   the new game, if the action is allowed
    */
  def assignRoad(road: RoadSpot): Option[ScatanGame] =
    play(AssignRoad)(using AssignRoadEffect(game.turn.player, road))

  /** Rolls the dice and applies the effects.
    * @param callback
    *   the callback to be called with the roll
    * @return
    *   the new game, if the action is allowed
    */
  def rollDice(callback: Int => Unit = x => ()): Option[ScatanGame] =
    val roll = Random.nextInt(6) + 1 + Random.nextInt(6) + 1
    callback(roll)
    roll match
      case 7 =>
        play(RollSeven)(using RollSevenEffect())
      case _ =>
        play(RollDice)(using RollEffect(roll))

  /** Places the robber on the given hex.
    * @param hex
    *   the hex
    * @return
    *   the new game, if the action is allowed
    */
  def placeRobber(hex: Hexagon): Option[ScatanGame] =
    def isPossibleToStealCard(game: ScatanGame): Boolean =
      game.playersOnRobber.filter(_ != game.turn.player).exists(game.state.resourceCards(_).sizeIs > 0)
    play(PlaceRobber)(using PlaceRobberEffect(hex)) match
      case Some(game) if !isPossibleToStealCard(game) =>
        game.skipStealCard
      case game => game

  /** Skip the steal card action, by playing an empty effect.
    * @return
    *   the new game, if the action is allowed
    */
  private[ScatanGameActions] def skipStealCard: Option[ScatanGame] =
    play(ScatanActions.StealCard)(using EmptyEffect)

  /** Steals a card from the given player.
    * @param player
    *   the player
    * @return
    *   the new game, if the action is allowed
    */
  def stealCard(player: ScatanPlayer): Option[ScatanGame] =
    play(StealCard)(using StealCardEffect(this.game.turn.player, player))

  /*
   * Build Ops
   */

  /** Builds a road on the given spot.
    * @param road
    *   the spot
    * @return
    *   the new game, if the action is allowed
    */
  def buildRoad(road: RoadSpot): Option[ScatanGame] =
    play(ScatanActions.BuildRoad)(using BuildRoadEffect(road, game.turn.player))

  /** Builds a settlement on the given spot.
    * @param spot
    *   the spot
    * @return
    *   the new game, if the action is allowed
    */
  def buildSettlement(spot: StructureSpot): Option[ScatanGame] =
    play(ScatanActions.BuildSettlement)(using BuildSettlementEffect(spot, game.turn.player))

  /** Builds a city on the given spot.
    * @param spot
    *   the spot
    * @return
    *   the new game, if the action is allowed
    */
  def buildCity(spot: StructureSpot): Option[ScatanGame] =
    play(ScatanActions.BuildCity)(using BuildCityEffect(spot, game.turn.player))

  /** Buy a development card.
    * @return
    *   the new game, if the action is allowed
    */
  def buyDevelopmentCard: Option[ScatanGame] =
    play(ScatanActions.BuyDevelopmentCard)(using BuyDevelopmentCardEffect(game.turn.player, game.turn.number))

  /** Plays a knight development card.
    * @param robberPosition
    *   the position of the robber
    * @return
    *   the new game, if the action is allowed
    */
  def playKnightDevelopment(robberPosition: Hexagon): Option[ScatanGame] =
    play(ScatanActions.PlayDevelopmentCard)(using
      PlayKnightDevelopmentCardEffect(game.turn.player, game.turn.number, robberPosition)
    )

  /** Plays a monopoly development card.
    * @param resourceType
    *   the resource type
    * @return
    *   the new game, if the action is allowed
    */
  def playMonopolyDevelopment(resourceType: ResourceType): Option[ScatanGame] =
    play(ScatanActions.PlayDevelopmentCard)(using
      PlayMonopolyDevelopmentCardEffect(game.turn.player, game.turn.number, resourceType)
    )

  /** Plays a year of plenty development card.
    * @param firstResourceType
    *   the first resource type
    * @param secondResourceType
    *   the second resource type
    * @return
    *   the new game, if the action is allowed
    */
  def playYearOfPlentyDevelopment(
      firstResourceType: ResourceType,
      secondResourceType: ResourceType
  ): Option[ScatanGame] =
    play(ScatanActions.PlayDevelopmentCard)(using
      PlayYearOfPlentyDevelopmentCardEffect(game.turn.player, game.turn.number, firstResourceType, secondResourceType)
    )

  /** Plays a road building development card.
    * @param firstRoad
    *   the first road
    * @param secondRoad
    *   the second road
    * @return
    *   the new game, if the action is allowed
    */
  def playRoadBuildingDevelopment(firstRoad: RoadSpot, secondRoad: RoadSpot): Option[ScatanGame] =
    play(ScatanActions.PlayDevelopmentCard)(using
      PlayRoadBuildingDevelopmentCardEffect(game.turn.player, game.turn.number, firstRoad, secondRoad)
    )

  /** Trades with the bank.
    * @param offer
    *   the offer
    * @param request
    *   the request
    * @return
    *   the new game, if the action is allowed
    */
  def tradeWithBank(offer: ResourceType, request: ResourceType): Option[ScatanGame] =
    play(ScatanActions.TradeWithBank)(using TradeWithBankEffect(game.turn.player, offer, request))

  /** Trades with the player.
    * @param receiver
    *   the receiver
    * @param senderTradeCards
    *   the cards the sender gives
    * @param receiverTradeCards
    *   the cards the receiver gives
    * @return
    *   the new game, if the action is allowed
    */
  def tradeWithPlayer(
      receiver: ScatanPlayer,
      senderTradeCards: Seq[ResourceCard],
      receiverTradeCards: Seq[ResourceCard]
  ): Option[ScatanGame] =
    play(ScatanActions.TradeWithPlayer)(using
      TradeWithPlayerEffect(game.turn.player, receiver, senderTradeCards, receiverTradeCards)
    )

class ScatanGame(game: Game[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer])
    extends ScatanGameStatus(game)
    with ScatanGameActions

object ScatanGame:
  def apply(game: Game[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer]): ScatanGame =
    new ScatanGame(game)
  def apply(gameMap: GameMap, players: Seq[ScatanPlayer]): ScatanGame =
    new ScatanGame(Game(gameMap, players)(using ScatanDSL.rules))
