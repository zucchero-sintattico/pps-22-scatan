package scatan.model.components

import cats.kernel.Semigroup
import scatan.model.game.config.ScatanPlayer

/** A Map that contains for each player the number of points they have
  */
type Scores = Map[ScatanPlayer, Int]

object Score:

  given Semigroup[Scores] with
    def combine(x: Scores, y: Scores): Scores =
      x ++ y.map { case (player, score) =>
        player -> (score + x.getOrElse(player, 0))
      }

  /** Returns a map of players to 0 points
    *
    * @param players
    *   the players to create the empty scores map for
    * @return
    *   the empty scores map
    */
  def empty(players: Seq[ScatanPlayer]): Scores =
    players.map(_ -> 0).toMap
