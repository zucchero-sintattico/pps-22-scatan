package scatan.model.game.config

enum ScatanActions:
  /** Change the current turn to the next player.
    */
  case NextTurn

  /** Assign a settlement to a player. This is used during the initial placement phase.
    */
  case AssignSettlement

  /** Assign a road to a player. This is used during the initial placement phase.
    */
  case AssignRoad

  /** Roll the dice and distribute resources. This is used during the main game.
    */
  case RollDice

  /** Roll a seven. This is used during the main game to switch to the robber placement phase.
    */
  case RollSeven

  /** Place the robber.
    */
  case PlaceRobber

  /** Steal a card from a player.
    */
  case StealCard

  /** Build a road.
    */
  case BuildRoad

  /** Build a settlement.
    */
  case BuildSettlement

  /** Build a city.
    */
  case BuildCity

  /** Buy a development card.
    */
  case BuyDevelopmentCard

  /** Play a development card.
    */
  case PlayDevelopmentCard

  /** Trade with the bank.
    */
  case TradeWithBank

  /** Trade with a player.
    */
  case TradeWithPlayer
