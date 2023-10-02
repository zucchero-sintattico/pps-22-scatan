package scatan.model.game.state
import scatan.model.components.Scores
import scatan.lib.game.Player
import scatan.model.game.state.BasicScatanState

trait StateWithScores extends BasicScatanState:
  def scores: Scores
  def winner: Option[Player] = if isOver then Some(scores.maxBy(_._2)._1) else None
  def isOver: Boolean = scores.exists(_._2 >= 10)
