package scatan.model

import scatan.model.Spot

/** The type of action that can be performed in a turn.
  */
enum ActionType:
  case Roll
  case PlaceRobber
  case StoleCard
  case Build
  case BuyDevelopmentCard
  case PlayDevelopmentCard
  case Trade
  case NextTurn

/** The effective action that can be performed in a turn.
  *
  * @param actionType
  *   the type of action
  * @param apply
  *   the function to apply the action to a game
  */
enum Action(val actionType: ActionType, val apply: Game => Game):

  /**   - Roll the dice.
    * \-
    */
  case Roll
      extends Action(
        ActionType.Roll,
        game =>
          game.copy(
            currentTurn = game.currentTurn.copy(phase = Phase.Playing)
          )
      )

  case PlaceRobber(val spot: Spot)
      extends Action(
        ActionType.PlaceRobber,
        game =>
          game.copy(
            currentTurn = game.currentTurn.copy(phase = Phase.StoleCard)
          )
      )

  case StoleCard(val player: Player)
      extends Action(
        ActionType.StoleCard,
        game =>
          game.copy(
            currentTurn = game.currentTurn.copy(phase = Phase.Playing)
          )
      )

  case Build(val spot: Spot, val buildingType: BuildingType)
      extends Action(
        ActionType.Build,
        game =>
          game.copy(
            currentTurn = game.currentTurn.copy(phase = Phase.Playing)
          )
      )

  case BuyDevelopmentCard
      extends Action(
        ActionType.BuyDevelopmentCard,
        game =>
          game.copy(
            currentTurn = game.currentTurn.copy(phase = Phase.Playing)
          )
      )

  case PlayDevelopmentCard(val developmentCard: DevelopmentCard)
      extends Action(
        ActionType.PlayDevelopmentCard,
        game =>
          game.copy(
            currentTurn = game.currentTurn.copy(phase = Phase.Playing)
          )
      )

  case Trade
      extends Action(
        ActionType.Trade,
        game =>
          game.copy(
            currentTurn = game.currentTurn.copy(phase = Phase.Playing)
          )
      )

  case NextTurn
      extends Action(
        ActionType.NextTurn,
        game =>
          val nextPlayer = game.players(game.currentTurn.number % game.players.size)
          game.copy(
            currentTurn = game.currentTurn.copy(
              number = game.currentTurn.number + 1,
              player = nextPlayer,
              phase = Phase.Initial
            )
          )
      )
