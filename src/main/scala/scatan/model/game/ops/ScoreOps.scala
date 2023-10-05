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
    private def partialScoresWithAwards: Scores =
      val playersWithAwards = state.awards.filter(_._2.isDefined).map(_._2.get)
      playersWithAwards.foldLeft(Score.empty(state.players))((scores, playerWithCount) =>
        scores.updated(playerWithCount._1, scores(playerWithCount._1) + 1)
      )

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

    def scores: Scores =
      import cats.syntax.semigroup.*
      import scatan.model.components.Score.given
      val partialScores = Seq(partialScoresWithAwards, partialScoresWithBuildings)

      partialScores.foldLeft(Score.empty(state.players))(_ |+| _)

    def isOver: Boolean = scores.exists(_._2 >= 10)
    def winner: Option[Player] = if isOver then Some(scores.maxBy(_._2)._1) else None
