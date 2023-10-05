package scatan.model.game

import scatan.model.GameMap
import scatan.model.components.*
import scatan.model.components.AssignedBuildingsAdapter.asPlayerMap
import scatan.model.game.config.ScatanPlayer
import scatan.model.map.*

object ScatanState:
  def apply(players: Seq[ScatanPlayer]): ScatanState =
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

final case class ScatanState(
    players: Seq[ScatanPlayer],
    gameMap: GameMap,
    assignedBuildings: AssignedBuildings,
    robberPlacement: Hexagon,
    resourceCards: ResourceCards,
    developmentCards: DevelopmentCards,
    assignedAwards: Awards = Award.empty()
):

  /** Returns a new ScatanState with the robber moved to the specified hexagon.
    *
    * @param hexagon
    *   the hexagon to move the robber to
    * @return
    *   a new ScatanState with the robber moved to the specified hexagon
    */
  def moveRobber(hexagon: Hexagon): ScatanState = this.copy(robberPlacement = hexagon)
