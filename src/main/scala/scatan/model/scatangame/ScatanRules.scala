package scatan.model.scatangame

import scatan.model.game.GameRuleDSL

object ScatanRules extends GameRuleDSL[ScatanPhases, ScatanActions]:
  import ScatanActions.*
  import ScatanPhases.*

  Players canBe (3 to 4)
  Start withPhase Initial
  Turn canEndIn Playing

  Phases(ScatanPhases.values*)

  When in Initial phase {
    case RollDice(7) => RobberPlacement
    case RollDice(_) => Playing
  }

  When in RobberPlacement phase { case PlaceRobber(_) =>
    Playing
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
