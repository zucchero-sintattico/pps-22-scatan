package scatan.model.game

import scatan.lib.game.ops.GamePlayOps.{allowedActions, play}
import scatan.lib.game.ops.GameTurnOps.nextTurn
import scatan.lib.game.ops.GameWinOps.{isOver, winner}
import scatan.lib.game.{Game, GameStatus, Rules, Turn}
import scatan.model.game.config.ScatanActions.{
  PlaceRobber,
  PlaceRobberEffect,
  RollDice,
  RollEffect,
  RollSeven,
  RollSevenEffect,
  StoleCard,
  StoleCardEffect
}
import scatan.model.game.config.{ScatanActions, ScatanPhases, ScatanPlayer, ScatanSteps}
import scatan.model.map.Hexagon

import scala.util.Random

trait ScatanGame(game: Game[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer]):
  // State
  def players: Seq[ScatanPlayer] = game.players
  def state: ScatanState = game.state
  def turn: Turn[ScatanPlayer] = game.turn
  def gameStatus: GameStatus[ScatanPhases, ScatanSteps] = game.gameStatus
  def isOver: Boolean = game.isOver
  def winner: Option[ScatanPlayer] = game.winner
  def nextTurn: Option[ScatanGame] = game.nextTurn.map(ScatanGame.apply)
  def allowedActions: Set[ScatanActions] = game.allowedActions.filter(_ != RollSeven)
  // Actions
  def rollDice: Option[ScatanGame]
  def placeRobber(hex: Hexagon): Option[ScatanGame]
  def stoleCard(player: ScatanPlayer): Option[ScatanGame]
  def buildRoad: Option[ScatanGame]
  def buildSettlement: Option[ScatanGame]
  def buildCity: Option[ScatanGame]
  def buildDevelopmentCard: Option[ScatanGame]
  def playDevelopmentCard: Option[ScatanGame]
  def tradeWithBank: Option[ScatanGame]
  def tradeWithPlayer: Option[ScatanGame]

private final case class ScatanGameImpl(game: Game[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer])
    extends ScatanGame(game):

  def rollDice: Option[ScatanGame] =
    val roll = Random.nextInt(6) + 1 + Random.nextInt(6) + 1
    roll match
      case 7 =>
        game.play(RollSeven)(using RollSevenEffect()).map(ScatanGame.apply)
      case _ =>
        game.play(RollDice)(using RollEffect(roll)).map(ScatanGame.apply)

  override def placeRobber(hex: Hexagon): Option[ScatanGame] =
    game.play(PlaceRobber)(using PlaceRobberEffect(hex)).map(ScatanGame.apply)

  override def stoleCard(player: ScatanPlayer): Option[ScatanGame] =
    game.play(StoleCard)(using StoleCardEffect(player)).map(ScatanGame.apply)

  override def buildRoad: Option[ScatanGame] = ???

  override def buildSettlement: Option[ScatanGame] = ???

  override def buildCity: Option[ScatanGame] = ???

  override def buildDevelopmentCard: Option[ScatanGame] = ???

  override def playDevelopmentCard: Option[ScatanGame] = ???

  override def tradeWithBank: Option[ScatanGame] = ???

  override def tradeWithPlayer: Option[ScatanGame] = ???

object ScatanGame:
  def apply(game: Game[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer]): ScatanGame =
    ScatanGameImpl(game)
  def apply(players: Seq[ScatanPlayer]): ScatanGame =
    ScatanGame(Game(players)(using ScatanDSL.rules))
