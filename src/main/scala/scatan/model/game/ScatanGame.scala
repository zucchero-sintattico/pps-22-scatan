package scatan.model.game

import scatan.lib.game.ops.GamePlayOps.{allowedActions, play}
import scatan.lib.game.ops.GameTurnOps.nextTurn
import scatan.lib.game.ops.GameWinOps.{isOver, winner}
import scatan.lib.game.{Game, GameStatus, Rules, Turn}
import scatan.model.game.config.ScatanActions.*
import scatan.model.game.config.{ScatanActions, ScatanPhases, ScatanPlayer, ScatanSteps}
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
  def nextTurn: Option[ScatanGame] = game.nextTurn.map(ScatanGame.apply)
  def allowedActions: Set[ScatanActions] = game.allowedActions.filter(_ != RollSeven)

private trait ScatanGameActions extends ScatanGameStatus:
  def rollDice: Option[ScatanGame] =
    val roll = Random.nextInt(6) + 1 + Random.nextInt(6) + 1
    roll match
      case 7 =>
        game.play(RollSeven)(using RollSevenEffect()).map(ScatanGame.apply)
      case _ =>
        game.play(RollDice)(using RollEffect(roll)).map(ScatanGame.apply)

  def placeRobber(hex: Hexagon): Option[ScatanGame] =
    game.play(PlaceRobber)(using PlaceRobberEffect(hex)).map(ScatanGame.apply)

  def stoleCard(player: ScatanPlayer): Option[ScatanGame] =
    game.play(StoleCard)(using StoleCardEffect(player)).map(ScatanGame.apply)

  def buildRoad(road: RoadSpot): Option[ScatanGame] = ???
  def buildSettlement(spot: StructureSpot): Option[ScatanGame] =
    game.play(ScatanActions.BuildSettlement)(using BuildSettlementEffect(spot, game.turn.player)).map(ScatanGame.apply)
  def buildCity: Option[ScatanGame] = ???
  def buildDevelopmentCard: Option[ScatanGame] = ???
  def playDevelopmentCard: Option[ScatanGame] = ???
  def tradeWithBank: Option[ScatanGame] = ???
  def tradeWithPlayer: Option[ScatanGame] = ???

class ScatanGame(game: Game[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer])
    extends ScatanGameStatus(game)
    with ScatanGameActions

object ScatanGame:
  def apply(game: Game[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer]): ScatanGame =
    new ScatanGame(game)
  def apply(players: Seq[ScatanPlayer]): ScatanGame =
    new ScatanGame(Game(players)(using ScatanDSL.rules))
