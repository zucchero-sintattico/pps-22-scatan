package scatan.model.game.config

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
