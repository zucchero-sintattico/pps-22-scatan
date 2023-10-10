package scatan.model.game.ops

import scatan.model.components.*
import scatan.model.components.DevelopmentCard
import scatan.model.components.AssignedBuildingsAdapter.asPlayerMap
import scatan.model.game.ScatanState
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.ops.AwardOps.*

object ScoreOps:
  extension (state: ScatanState)

    /** Computes the partial scores with awards for the current game state. A player is awarded a point for each award
      * they have received.
      * @return
      *   the partial scores with awards for each player
      */
    private def partialScoresWithAwards: Scores =
      val playersWithAwards = state.awards.filter(_._2.isDefined).map(_._2.get)
      playersWithAwards.foldLeft(Scores.empty(state.players))((scores, playerWithCount) =>
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
      state.assignedBuildings.asPlayerMap.foldLeft(Scores.empty(state.players))((scores, buildingsOfPlayer) =>
        scores.updated(
          buildingsOfPlayer._1,
          buildingsOfPlayer._2.foldLeft(0)((score, buildingType) => score + buildingScore(buildingType))
        )
      )

    /** Computes the partial scores of each player, taking into account the development cards they have assigned. A
      * victory point card is worth 1 point.
      *
      * @return
      *   a Scores object containing the partial scores of each player.
      */
    private def partialScoresWithVictoryPointCards: Scores =
      val playersWithVictoryPointCards =
        state.developmentCards.filter(_._2.exists(_.developmentType == DevelopmentType.VictoryPoint)).map(_._1)
      playersWithVictoryPointCards.foldLeft(Scores.empty(state.players))((scores, player) =>
        scores.updated(player, scores(player) + 1)
      )

    /** This method calculates the total scores of all players in the game by combining the partial scores with awards
      * and buildings. It uses the `|+|` operator from the `cats.syntax.semigroup` package to combine the scores.
      * @return
      *   the total scores of all players in the game.
      */
    def scores: Scores =
      import cats.syntax.semigroup.*
      import scatan.model.components.Scores.given
      val partialScores = Seq(partialScoresWithAwards, partialScoresWithBuildings, partialScoresWithVictoryPointCards)
      partialScores.foldLeft(Scores.empty(state.players))(_ |+| _)

    /** Returns true if the game is over, false otherwise. The game is over when a player has 10 or more points.
      * @return
      *   true if the game is over, false otherwise
      */
    def isOver: Boolean = scores.exists(_._2 >= 10)

    /** Returns the winner of the game if the game is over, None otherwise.
      * @return
      *   the winner of the game if the game is over, None otherwise
      */
    def winner: Option[ScatanPlayer] = if isOver then Some(scores.maxBy(_._2)._1) else None
