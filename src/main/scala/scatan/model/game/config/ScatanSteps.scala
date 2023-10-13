package scatan.model.game.config

enum ScatanSteps:
  /** The game is in the process of changing turns. This is a special state that is used to trigger turn change event.
    */
  case ChangingTurn

  /** The game is in the initial setup phase. Players are placing their initial road.
    */
  case SetupRoad

  /** The game is in the initial setup phase. Players are placing their initial settlement.
    */
  case SetupSettlement

  /** The game is in the game phase. This is when players start their turns.
    */
  case Starting

  /** The game is in the game phase. This is when players have to place the robber.
    */
  case PlaceRobber

  /** The game is in the game phase. This is when players have to steal a card.
    */
  case StealCard

  /** The game is in the game phase. This is when players are rolled the dice and are free to trade and build.
    */
  case Playing
