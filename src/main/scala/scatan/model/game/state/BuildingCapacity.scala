package scatan.model.game.state

import scatan.model.map.Spot
import scatan.model.components.BuildingType
import scatan.lib.game.Player

trait BuildingCapacity extends BasicScatanState:
  def build(position: Spot, buildingType: BuildingType, player: Player): ScatanState
  def assignBuilding(position: Spot, buildingType: BuildingType, player: Player): ScatanState
