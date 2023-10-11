package scatan.model.game.ops

import scatan.model.components.AssignedBuildingsAdapter.asPlayerMap
import scatan.model.components.*
import scatan.model.game.ScatanState
import scatan.model.game.config.ScatanPlayer

object AwardOps:

  extension (state: ScatanState)
    /** Returns a map of the current awards and their respective players. The awards are Longest Road and Largest Army.
      * Longest Road is awarded to the player with the longest continuous road of at least 5 segments. Largest Army is
      * awarded to the player with the most Knight development cards played.
      * @return
      *   a map of the current awards and their respective players.
      */
    def awards: Awards =
      val precedentLongestRoad = state.assignedAwards(Award(AwardType.LongestRoad))
      val longestRoad =
        state.assignedBuildings.asPlayerMap.foldLeft(precedentLongestRoad.getOrElse((ScatanPlayer(""), 0)))(
          (playerWithLongestRoad, buildingsOfPlayer) =>
            val roads = buildingsOfPlayer._2.filter(_ == BuildingType.Road)
            if roads.sizeIs > playerWithLongestRoad._2 then (buildingsOfPlayer._1, roads.size)
            else playerWithLongestRoad
        )
      val precedentLargestArmy = state.assignedAwards(Award(AwardType.LargestArmy))
      val largestArmy =
        state.developmentCards.foldLeft(precedentLargestArmy.getOrElse(ScatanPlayer(""), 0))(
          (playerWithLargestArmy, cardsOfPlayer) =>
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
