package scatan.model.game.state

import scatan.lib.game.Player
import scatan.model.GameMap
import scatan.model.components.ResourceCards
import scatan.model.map.Spot
import scatan.model.components.AssignedBuildings
import scatan.model.components.DevelopmentCards
import scatan.model.components.Awards
import scatan.model.components.Scores
import scatan.model.game.ScatanState
import scatan.model.components.BuildingType
import scatan.model.components.ResourceCard
import scatan.model.components.DevelopmentCard

trait BasicScatanState:
  def players: Seq[Player]
  def gameMap: GameMap
  def resourceCards: ResourceCards
  def emptySpot: Seq[Spot]
  def assignedBuildings: AssignedBuildings
  def developmentCards: DevelopmentCards
  def awards: Awards
  def scores: Scores
  def assignResourceCard(player: Player, resourceCard: ResourceCard): ScatanState
  def assignDevelopmentCard(player: Player, developmentCard: DevelopmentCard): ScatanState
  def consumeDevelopmentCard(player: Player, developmentCard: DevelopmentCard): ScatanState
