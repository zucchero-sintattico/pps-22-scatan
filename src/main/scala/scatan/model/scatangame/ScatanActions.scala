package scatan.model.scatangame

import scatan.model.game.{Action, Game}
import scatan.model.map.Hexagon

enum ScatanActions(effect: ScatanState => ScatanState = identity) extends Action(effect):
  case RollDice(result: Int) extends ScatanActions
  case PlaceRobber(hexagon: Hexagon) extends ScatanActions
  case StoleCard(player: String) extends ScatanActions
  case BuildRoad extends ScatanActions
  case BuildSettlement extends ScatanActions
  case BuildCity extends ScatanActions
  case BuyDevelopmentCard extends ScatanActions
  case PlayDevelopmentCard extends ScatanActions
  case TradeWithBank extends ScatanActions
  case TradeWithPlayer extends ScatanActions
