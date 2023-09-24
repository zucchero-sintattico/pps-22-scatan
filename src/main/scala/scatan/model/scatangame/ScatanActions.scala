package scatan.model.scatangame

import scatan.model.game.Action
import scatan.model.map.Hexagon

enum ScatanActions(effect: ScatanState => ScatanState) extends Action[ScatanState](effect):
  case RollDice(result: Int) extends ScatanActions(ScatanActions.RollEffect(result))
  case PlaceRobber(hexagon: Hexagon) extends ScatanActions(ScatanActions.PlaceRobberEffect(hexagon))
  case StoleCard(player: String) extends ScatanActions(ScatanActions.StoleCardEffect(player))
  case BuildRoad extends ScatanActions(ScatanActions.BuildRoadEffect)
  case BuildSettlement extends ScatanActions(ScatanActions.BuildSettlementEffect)
  case BuildCity extends ScatanActions(ScatanActions.BuildCityEffect)
  case BuyDevelopmentCard extends ScatanActions(ScatanActions.BuyDevelopmentCardEffect)
  case PlayDevelopmentCard extends ScatanActions(ScatanActions.PlayDevelopmentCardEffect)
  case TradeWithBank extends ScatanActions(ScatanActions.TradeWithBankEffect)
  case TradeWithPlayer extends ScatanActions(ScatanActions.TradeWithPlayerEffect)

object ScatanActions:
  private def RollEffect(result: Int): ScatanState => ScatanState = identity
  private def PlaceRobberEffect(hexagon: Hexagon): ScatanState => ScatanState = identity
  private def StoleCardEffect(player: String): ScatanState => ScatanState = identity
  private def BuildRoadEffect: ScatanState => ScatanState = identity
  private def BuildSettlementEffect: ScatanState => ScatanState = identity
  private def BuildCityEffect: ScatanState => ScatanState = identity
  private def BuyDevelopmentCardEffect: ScatanState => ScatanState = identity
  private def PlayDevelopmentCardEffect: ScatanState => ScatanState = identity
  private def TradeWithBankEffect: ScatanState => ScatanState = identity
  private def TradeWithPlayerEffect: ScatanState => ScatanState = identity
