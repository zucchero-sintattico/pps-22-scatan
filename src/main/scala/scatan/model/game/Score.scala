package scatan.model.game

/** A Map that contains for each player the number of points they have
  */
type Scores = Map[Player, Int]

object Score:
  /** Returns a map of players to 0 points
    *
    * @param players
    *   the players to create the empty scores map for
    * @return
    *   the empty scores map
    */
  def empty(players: Seq[Player]): Scores =
    players.map(_ -> 0).toMap
