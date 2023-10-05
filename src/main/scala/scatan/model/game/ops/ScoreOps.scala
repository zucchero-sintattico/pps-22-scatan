package scatan.model.game.ops

import scatan.model.components.Scores
import scatan.lib.game.Player
import scatan.model.components.Score
import scatan.model.components.BuildingType
import scatan.model.components.AssignedBuildingsAdapter.asPlayerMap
import scatan.model.components.Awards
import scatan.model.components.Award
import scatan.model.components.AwardType
import scatan.model.components.DevelopmentType
import scatan.model.game.ScatanState

object ScoreOps:
  extension (state: ScatanState)

    /** Computes the partial scores with awards for the current game state. A player is awarded a point for each award
      * they have received.
      * @return
      *   the partial scores with awards for each player
      */
    private def partialScoresWithAwards: Scores =
      val playersWithAwards = state.awards.filter(_._2.isDefined).map(_._2.get)
      playersWithAwards.foldLeft(Score.empty(state.players))((scores, playerWithCount) =>
        scores.updated(playerWithCount._1, scores(playerWithCount._1) + 1)
      )

    /** Computes the partial scores of each player, taking into account the buildings they have assigned. A settlement
      * is worth 1 point, a city is worth 2 points, and a road is worth 0 points.
      * @return
      *   a Scores object containing the partial scores of each player.
      */
    private def partialScoresWithBuildings: Scores =
      def buildingScore(buildingType: BuildingType): Int = buildingType match
        case BuildingType.Settlement => 1
        case BuildingType.City       => 2
        case BuildingType.Road       => 0
      state.assignedBuildings.asPlayerMap.foldLeft(Score.empty(state.players))((scores, buildingsOfPlayer) =>
        scores.updated(
          buildingsOfPlayer._1,
          buildingsOfPlayer._2.foldLeft(0)((score, buildingType) => score + buildingScore(buildingType))
        )
      )

    /** This method calculates the total scores of all players in the game by combining the partial scores with awards
      * and buildings. It uses the `|+|` operator from the `cats.syntax.semigroup` package to combine the scores.
      * @return
      *   the total scores of all players in the game.
      */
    def scores: Scores =
      import cats.syntax.semigroup.*
      import scatan.model.components.Score.given
      val partialScores = Seq(partialScoresWithAwards, partialScoresWithBuildings)
      partialScores.foldLeft(Score.empty(state.players))(_ |+| _)

    def isOver: Boolean = scores.exists(_._2 >= 10)
    def winner: Option[Player] = if isOver then Some(scores.maxBy(_._2)._1) else None
