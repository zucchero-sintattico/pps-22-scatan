package scatan.model.game.state

import scatan.model.components.Scores
import scatan.lib.game.Player
import scatan.model.components.Score
import scatan.model.components.BuildingType
import scatan.model.components.AssignedBuildingsAdapter.asPlayerMap
import scatan.model.components.Awards
import scatan.model.components.Award
import scatan.model.components.AwardType
import scatan.model.components.DevelopmentType

trait AwardKnowledge[S <: AwardKnowledge[S]] extends BasicScatanState[S]:
  def assignedAwards: Awards
  def awards: Awards =
    val precedentLongestRoad = assignedAwards(Award(AwardType.LongestRoad))
    val longestRoad =
      assignedBuildings.asPlayerMap.foldLeft(precedentLongestRoad.getOrElse((Player(""), 0)))(
        (playerWithLongestRoad, buildingsOfPlayer) =>
          val roads = buildingsOfPlayer._2.filter(_ == BuildingType.Road)
          if roads.sizeIs > playerWithLongestRoad._2 then (buildingsOfPlayer._1, roads.size)
          else playerWithLongestRoad
      )
    val precedentLargestArmy = assignedAwards(Award(AwardType.LargestArmy))
    val largestArmy =
      developmentCards.foldLeft(precedentLargestArmy.getOrElse(Player(""), 0))((playerWithLargestArmy, cardsOfPlayer) =>
        val knights = cardsOfPlayer._2.filter(_.developmentType == DevelopmentType.Knight)
        if knights.sizeIs > playerWithLargestArmy._2 then (cardsOfPlayer._1, knights.size)
        else playerWithLargestArmy
      )
    Map(
      Award(AwardType.LongestRoad) -> (if longestRoad._2 >= 5 then Some((longestRoad._1, longestRoad._2))
                                       else precedentLongestRoad),
      Award(AwardType.LargestArmy) -> (if largestArmy._2 >= 3 then Some((largestArmy._1, largestArmy._2))
                                       else precedentLargestArmy)
    )

trait ScoreKnowledge[S <: ScoreKnowledge[S]] extends BasicScatanState[S]:

  private def partialScoresWithAwards: Scores =
    val playersWithAwards = awards.filter(_._2.isDefined).map(_._2.get)
    playersWithAwards.foldLeft(Score.empty(players))((scores, playerWithCount) =>
      scores.updated(playerWithCount._1, scores(playerWithCount._1) + 1)
    )

  private def partialScoresWithBuildings: Scores =
    def buildingScore(buildingType: BuildingType): Int = buildingType match
      case BuildingType.Settlement => 1
      case BuildingType.City       => 2
      case BuildingType.Road       => 0
    assignedBuildings.asPlayerMap.foldLeft(Score.empty(players))((scores, buildingsOfPlayer) =>
      scores.updated(
        buildingsOfPlayer._1,
        buildingsOfPlayer._2.foldLeft(0)((score, buildingType) => score + buildingScore(buildingType))
      )
    )

  def scores: Scores =
    import cats.syntax.semigroup.*
    import scatan.model.components.Score.given
    val partialScores = Seq(partialScoresWithAwards, partialScoresWithBuildings)

    partialScores.foldLeft(Score.empty(players))(_ |+| _)

  def isOver: Boolean = scores.exists(_._2 >= 10)
  def winner: Option[Player] = if isOver then Some(scores.maxBy(_._2)._1) else None
