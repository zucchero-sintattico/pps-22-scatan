package scatan.model.scatangame

import scatan.model.game.Action
import scatan.model.map.Hexagon

enum ScatanActions(effect: ScatanState => ScatanState = identity) extends Action(effect):
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
  private def RollEffect(result: Int): ScatanState => ScatanState = ???
  private def PlaceRobberEffect(hexagon: Hexagon): ScatanState => ScatanState = ???
  private def StoleCardEffect(player: String): ScatanState => ScatanState = ???
  private def BuildRoadEffect: ScatanState => ScatanState = ???
  private def BuildSettlementEffect: ScatanState => ScatanState = ???
  private def BuildCityEffect: ScatanState => ScatanState = ???
  private def BuyDevelopmentCardEffect: ScatanState => ScatanState = ???
  private def PlayDevelopmentCardEffect: ScatanState => ScatanState = ???
  private def TradeWithBankEffect: ScatanState => ScatanState = ???
  private def TradeWithPlayerEffect: ScatanState => ScatanState = ???
