package scatan.model.game.config

enum ScatanSteps:
  case ChangingTurn

  case SetupRoad
  case SetupSettlement

  case Starting
  case PlaceRobber
  case StealCard
  case Playing
