package scatan.model.game.state

import scatan.lib.game.Player
import scatan.model.map.Spot
import scatan.model.map.StructureSpot
import scatan.model.map.RoadSpot
import scatan.model.components.AssignedBuildings
import scatan.model.map.Hexagon
import scatan.model.components.DevelopmentCards
import scatan.model.components.ResourceCards
import scatan.model.components.Awards
import scatan.model.GameMap
import scatan.model.components.Scores
import scatan.model.components.BuildingType
import scatan.model.components.ResourceCard
import scatan.model.components.DevelopmentCard

trait BasicScatanState[S <: BasicScatanState[S]]:
  def players: Seq[Player]
  def gameMap: GameMap
  def assignedBuildings: AssignedBuildings
  def robberPlacement: Hexagon
  def developmentCards: DevelopmentCards
  def resourceCards: ResourceCards
  def awards: Awards
  def moveRobber(hexagon: Hexagon): S
  def assignResourcesFromNumber(diceRoll: Int): S
  def assignResourceCard(player: Player, resourceCard: ResourceCard): S
  def assignDevelopmentCard(player: Player, developmentCard: DevelopmentCard): S
  def consumeDevelopmentCard(player: Player, developmentCard: DevelopmentCard): S
