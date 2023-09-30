package scatan.model.game

import scatan.lib.game.GameRulesDSL
import scatan.model.game.{ScatanActions, ScatanPhases, ScatanState}

object ScatanRules extends GameRulesDSL[ScatanState, ScatanPhases, ScatanActions]:
  import ScatanActions.*
  import ScatanPhases.*

  Players canBe (3 to 4)
  Start withState { players => ScatanState(players) }
  Start withPhase InitialSettlmentAssignment
  Turn canEndIn Playing

  When in InitialSettlmentAssignment phase {
    case BuildSettlement(_, _)     => InitialRoadAssignment
    case EndInitialAssignmentPhase => Initial
  }

  When in InitialRoadAssignment phase { case BuildRoad(_, _) =>
    InitialSettlmentAssignment
  }

  When in Initial phase {
    case RollDice(7) => RobberPlacement
    case RollDice(_) => Playing
  }

  When in RobberPlacement phase { case PlaceRobber(_) =>
    CardStealing
  }

  When in CardStealing phase { case StoleCard(_) =>
    Playing
  }

  When in Playing phase {
    case BuildCity             => Playing
    case BuildRoad(_, _)       => Playing
    case BuildSettlement(_, _) => Playing
    case BuyDevelopmentCard    => Playing
    case PlayDevelopmentCard   => Playing
    case TradeWithBank         => Playing
    case TradeWithPlayer       => Playing
  }
