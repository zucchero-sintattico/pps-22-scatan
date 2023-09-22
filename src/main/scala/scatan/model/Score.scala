package scatan.model

type Scores = Map[Player, Int]

object Score:
  def EmptyScores(players: Seq[Player]): Scores =
    players.map(_ -> 0).toMap
