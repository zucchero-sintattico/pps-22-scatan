package scatan.model.game

import scatan.lib.game.ops.Effect
import scatan.lib.game.ops.GamePlayOps.{allowedActions, play}
import scatan.lib.game.ops.GameTurnOps.nextTurn
import scatan.lib.game.ops.GameWinOps.{isOver, winner}
import scatan.lib.game.{Game, GameStatus, Turn}
import scatan.model.game.ScatanEffects.*
import scatan.model.game.config.ScatanActions.*
import scatan.model.game.config.{ScatanActions, ScatanPhases, ScatanPlayer, ScatanSteps}
import scatan.model.map.{Hexagon, RoadSpot, StructureSpot}

import scala.util.Random
import scatan.model.components.ResourceCard

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

private trait ScatanGameActions extends ScatanGameStatus:

  private def play(action: ScatanActions)(using effect: Effect[action.type, ScatanState]): Option[ScatanGame] =
    game.play(action).map(ScatanGame.apply)

  def nextTurn: Option[ScatanGame] =
    play(ScatanActions.NextTurn)(using NextTurnEffect())

  /*
   * Assign Ops
   */

  def assignSettlement(spot: StructureSpot): Option[ScatanGame] =
    play(AssignSettlement)(using AssignSettlementEffect(game.turn.player, spot))

  def assignRoad(road: RoadSpot): Option[ScatanGame] =
    play(AssignRoad)(using AssignRoadEffect(game.turn.player, road))

  def rollDice(callback: Int => Unit = x => ()): Option[ScatanGame] =
    val roll = Random.nextInt(6) + 1 + Random.nextInt(6) + 1
    callback(roll)
    roll match
      case 7 =>
        play(RollSeven)(using RollSevenEffect())
      case _ =>
        play(RollDice)(using RollEffect(roll))

  def placeRobber(hex: Hexagon): Option[ScatanGame] =
    play(PlaceRobber)(using PlaceRobberEffect(hex))

  def stoleCard(player: ScatanPlayer): Option[ScatanGame] =
    play(StoleCard)(using StoleCardEffect(player))

  /*
   * Build Ops
   */

  def buildRoad(road: RoadSpot): Option[ScatanGame] =
    play(ScatanActions.BuildRoad)(using BuildRoadEffect(road, game.turn.player))

  def buildSettlement(spot: StructureSpot): Option[ScatanGame] =
    play(ScatanActions.BuildSettlement)(using BuildSettlementEffect(spot, game.turn.player))

  def buildCity(spot: StructureSpot): Option[ScatanGame] =
    play(ScatanActions.BuildCity)(using BuildCityEffect(spot, game.turn.player))

  def buyDevelopmentCard: Option[ScatanGame] =
    play(ScatanActions.BuyDevelopmentCard)(using BuyDevelopmentCardEffect(game.turn.player, game.turn.number))

  def playDevelopmentCard: Option[ScatanGame] = ???
  def tradeWithBank: Option[ScatanGame] = ???
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
  def apply(players: Seq[ScatanPlayer]): ScatanGame =
    new ScatanGame(Game(players)(using ScatanDSL.rules))
