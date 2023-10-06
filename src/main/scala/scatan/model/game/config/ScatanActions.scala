package scatan.model.game.config

import scatan.lib.game.ops.Effect
import scatan.model.components.BuildingType
import scatan.model.game.ScatanState
import scatan.model.game.ops.BuildingOps.assignBuilding
import scatan.model.map.{Hexagon, StructureSpot}

enum ScatanActions:
  // Setup
  case AssignSettlement
  case AssignRoad
  // Game
  case RollDice
  case RollSeven
  case PlaceRobber
  case StoleCard
  case BuildRoad
  case BuildSettlement
  case BuildCity
  case BuyDevelopmentCard
  case PlayDevelopmentCard
  case TradeWithBank
  case TradeWithPlayer
