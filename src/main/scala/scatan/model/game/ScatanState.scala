package scatan.model.game

import scatan.model.GameMap
import scatan.model.components.*
import scatan.model.components.AssignedBuildingsAdapter.asPlayerMap
import scatan.model.game.config.ScatanPlayer
import scatan.model.map.*

/** Represents the state of a Scatan game.
  *
  * @param players
  *   The players in the game.
  * @param gameMap
  *   The game map.
  * @param assignedBuildings
  *   The buildings assigned to each player.
  * @param robberPlacement
  *   The current placement of the robber.
  * @param resourceCards
  *   The resource cards in the game.
  * @param developmentCards
  *   The development cards in the game.
  * @param assignedAwards
  *   The awards assigned to each player.
  */
final case class ScatanState(
    players: Seq[ScatanPlayer],
    gameMap: GameMap,
    assignedBuildings: AssignedBuildings,
    robberPlacement: Hexagon,
    resourceCards: ResourceCards,
    developmentCards: DevelopmentCards,
    assignedAwards: Awards = Award.empty()
)

object ScatanState:

  /** Creates a new ScatanState with the specified players.
    *
    * @param players
    * @return
    *   a new ScatanState with the specified players
    */
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
