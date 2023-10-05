package scatan.model.game

import cats.instances.long
import scatan.lib.game.Player
import scatan.model.components.*
import scatan.model.game.ScatanState
import scatan.model.GameMap
import scatan.model.map.Spot
import scatan.model.components.AssignedBuildingsAdapter.asPlayerMap
import scatan.model.map.Hexagon
import scatan.model.map.TileContent
import scatan.model.map.StructureSpot
import scatan.model.components.AssignedBuildingsAdapter.getStructureSpots
import scatan.model.map.HexagonInMap.layer
import scatan.model.map.RoadSpot
import scatan.model.game.ops.BasicScatanState
import scatan.model.game.ops.ScoreOps
import scatan.model.game.ops.EmptySpotsOps

object ScatanState:
  def apply(players: Seq[Player]): ScatanState =
    require(players.sizeIs >= 3 && players.sizeIs <= 4, "The number of players must be between 3 and 4")
    ScatanState(
      players,
      GameMap(),
      Map.empty,
      Hexagon(0, 0, 0),
      ResourceCard.empty(players),
      DevelopmentCardsOfPlayers.empty(players),
      Award.empty()
    )

  def ended(_players: Seq[Player]) =
    ScatanState(
      _players,
      GameMap(),
      Map.empty,
      Hexagon(0, 0, 0),
      ResourceCard.empty(_players),
      DevelopmentCardsOfPlayers.empty(_players),
      Award.empty()
    )
final case class ScatanState(
    players: Seq[Player],
    gameMap: GameMap,
    assignedBuildings: AssignedBuildings,
    robberPlacement: Hexagon,
    resourceCards: ResourceCards,
    developmentCards: DevelopmentCards,
    assignedAwards: Awards = Award.empty()
) extends BasicScatanState[ScatanState]:

  /** Returns a new ScatanState with the robber moved to the specified hexagon.
    *
    * @param hexagon
    *   the hexagon to move the robber to
    * @return
    *   a new ScatanState with the robber moved to the specified hexagon
    */
  def moveRobber(hexagon: Hexagon): ScatanState = this.copy(robberPlacement = hexagon)

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

  def isOver: Boolean = true

  def winner: Option[Player] =
    Option.empty[Player]
