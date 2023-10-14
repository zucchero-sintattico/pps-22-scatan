package scatan.model.game.state

import scatan.model.map.GameMap
import scatan.model.components.*
import scatan.model.components.UnproductiveTerrain.Desert
import scatan.model.game.DevelopmentCardsDeck
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
    assignedAwards: Awards,
    resourceCards: ResourceCards,
    developmentCards: DevelopmentCards,
    developmentCardsDeck: DevelopmentCardsDeck,
    robberPlacement: Hexagon
)

object ScatanState:

  /** Creates a new ScatanState with the specified players.
    *
    * @param players
    *   The players in the game.
    * @return
    *   a new ScatanState with the specified players
    */
  def apply(players: Seq[ScatanPlayer]): ScatanState =
    ScatanState(GameMap(), players, DevelopmentCardsDeck.defaultOrdered)

  def apply(
      gameMap: GameMap = GameMap(),
      players: Seq[ScatanPlayer],
      developmentCardsDeck: DevelopmentCardsDeck = DevelopmentCardsDeck.defaultOrdered
  ): ScatanState =
    require(players.sizeIs >= 3 && players.sizeIs <= 4, "The number of players must be between 3 and 4")
    val desertHexagon = gameMap.tiles.find(gameMap.toContent(_).terrain == Desert).get
    ScatanState(
      players,
      gameMap,
      AssignedBuildings.empty,
      Awards.empty,
      ResourceCards.empty(players),
      DevelopmentCards.empty(players),
      developmentCardsDeck,
      desertHexagon
    )
