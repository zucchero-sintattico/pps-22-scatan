package scatan.model.game

import scatan.lib.game.Action
import scatan.model.game.{ScatanActions, ScatanState}
import scatan.model.map.Hexagon
import scatan.model.map.{RoadSpot, StructureSpot}
import scatan.lib.game.Player
import scatan.model.components.BuildingType

enum ScatanActions(effect: ScatanState => ScatanState) extends Action[ScatanState](effect):
  case RollDice(result: Int) extends ScatanActions(ScatanActions.RollEffect(result))
  case PlaceRobber(hexagon: Hexagon) extends ScatanActions(ScatanActions.PlaceRobberEffect(hexagon))
  case StoleCard(player: String) extends ScatanActions(ScatanActions.StoleCardEffect(player))
  case BuildRoad(spot: RoadSpot, player: Player) extends ScatanActions(ScatanActions.BuildRoadEffect(spot, player))
  case BuildSettlement(spot: StructureSpot, player: Player)
      extends ScatanActions(ScatanActions.BuildSettlementEffect(spot, player))
  case BuildCity extends ScatanActions(ScatanActions.BuildCityEffect)
  case BuyDevelopmentCard extends ScatanActions(ScatanActions.BuyDevelopmentCardEffect)
  case PlayDevelopmentCard extends ScatanActions(ScatanActions.PlayDevelopmentCardEffect)
  case TradeWithBank extends ScatanActions(ScatanActions.TradeWithBankEffect)
  case TradeWithPlayer extends ScatanActions(ScatanActions.TradeWithPlayerEffect)
  case EndInitialAssignmentPhase extends ScatanActions(identity)

object ScatanActions:
  private def RollEffect(result: Int): ScatanState => ScatanState =
    // choose a random number between 1 and 6
    val result = scala.util.Random.between(1, 7)
    _.assignResourcesFromNumber(result)
  private def PlaceRobberEffect(hexagon: Hexagon): ScatanState => ScatanState = identity
  private def StoleCardEffect(player: String): ScatanState => ScatanState = identity
  private def BuildRoadEffect(spot: RoadSpot, player: Player): ScatanState => ScatanState =
    _.assignBuilding(spot, BuildingType.Road, player)
  private def BuildSettlementEffect(spot: StructureSpot, player: Player): ScatanState => ScatanState =
    _.assignBuilding(spot, BuildingType.Settlement, player)
  private def BuildCityEffect: ScatanState => ScatanState = identity
  private def BuyDevelopmentCardEffect: ScatanState => ScatanState = identity
  private def PlayDevelopmentCardEffect: ScatanState => ScatanState = identity
  private def TradeWithBankEffect: ScatanState => ScatanState = identity
  private def TradeWithPlayerEffect: ScatanState => ScatanState = identity
