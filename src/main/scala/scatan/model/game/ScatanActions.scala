package scatan.model.game

import scatan.lib.game.ops.Effect
import scatan.model.game.{ScatanActions, ScatanState}
import scatan.model.map.Hexagon

enum ScatanActions:
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

object ScatanActions:
  import ScatanActions.*

  given RollEffect: Effect[RollDice.type, ScatanState] with
    def apply(state: ScatanState): Option[ScatanState] = ???
