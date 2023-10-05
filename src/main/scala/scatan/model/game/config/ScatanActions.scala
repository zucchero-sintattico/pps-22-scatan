package scatan.model.game.config

import scatan.lib.game.ops.Effect
import scatan.model.game.ScatanState
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

  def RollEffect(result: Int) = new Effect[RollDice.type, ScatanState]:
    require(result != 7, "Use RollSevenEffect for rolling a 7")
    def apply(state: ScatanState): Option[ScatanState] = Some(state)

  def RollSevenEffect() = new Effect[RollSeven.type, ScatanState]:
    def apply(state: ScatanState): Option[ScatanState] = Some(state)

  def PlaceRobberEffect(hex: Hexagon) = new Effect[PlaceRobber.type, ScatanState]:
    def apply(state: ScatanState): Option[ScatanState] = Some(state)

  def StoleCardEffect(player: ScatanPlayer) = new Effect[StoleCard.type, ScatanState]:
    def apply(state: ScatanState): Option[ScatanState] = Some(state)

  def BuildRoadEffect() = new Effect[BuildRoad.type, ScatanState]:
    def apply(state: ScatanState): Option[ScatanState] = Some(state)

  def BuildSettlementEffect() = new Effect[BuildSettlement.type, ScatanState]:
    def apply(state: ScatanState): Option[ScatanState] = Some(state)

  def BuildCityEffect() = new Effect[BuildCity.type, ScatanState]:
    def apply(state: ScatanState): Option[ScatanState] = Some(state)

  def BuyDevelopmentCardEffect() = new Effect[BuyDevelopmentCard.type, ScatanState]:
    def apply(state: ScatanState): Option[ScatanState] = Some(state)

  def PlayDevelopmentCardEffect() = new Effect[PlayDevelopmentCard.type, ScatanState]:
    def apply(state: ScatanState): Option[ScatanState] = Some(state)

  def TradeWithBankEffect() = new Effect[TradeWithBank.type, ScatanState]:
    def apply(state: ScatanState): Option[ScatanState] = Some(state)

  def TradeWithPlayerEffect() = new Effect[TradeWithPlayer.type, ScatanState]:
    def apply(state: ScatanState): Option[ScatanState] = Some(state)
