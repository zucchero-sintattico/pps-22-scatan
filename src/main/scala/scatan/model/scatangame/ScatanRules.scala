package scatan.model.scatangame

import scatan.model.game.GameRulesDSL

object ScatanRules extends GameRulesDSL[ScatanState, ScatanPhases, ScatanActions]:
  import ScatanActions.*
  import ScatanPhases.*

  Players canBe (3 to 4)
  Start withState ScatanState()
  Start withPhase Initial
  Turn canEndIn Playing

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
    case BuildCity           => Playing
    case BuildRoad           => Playing
    case BuildSettlement     => Playing
    case BuyDevelopmentCard  => Playing
    case PlayDevelopmentCard => Playing
    case TradeWithBank       => Playing
    case TradeWithPlayer     => Playing
  }
