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

  /**   - Roll the dice. \-
    */
  case Roll(diceResult: Int = (1 + scala.util.Random.nextInt(6)) + (1 + scala.util.Random.nextInt(6)))
      extends Action(
        ActionType.Roll,
        game =>
          if diceResult == 7 then
            Game(
              players = game.players,
              currentTurn = Turn(
                game.currentTurn.number,
                game.currentTurn.currentPlayer,
                Phase.PlaceRobber
              ),
              isOver = game.isOver
            )
          else
            Game(
              players = game.players,
              currentTurn = Turn(
                game.currentTurn.number,
                game.currentTurn.currentPlayer,
                Phase.Playing
              ),
              isOver = game.isOver
            )
      )

  case PlaceRobber(val spot: Spot)
      extends Action(
        ActionType.PlaceRobber,
        game =>
          Game(
            players = game.players,
            currentTurn = Turn(
              game.currentTurn.number,
              game.currentTurn.currentPlayer,
              Phase.StoleCard
            ),
            isOver = game.isOver
          )
      )

  case StoleCard(val player: Player)
      extends Action(
        ActionType.StoleCard,
        game =>
          Game(
            players = game.players,
            currentTurn = Turn(
              game.currentTurn.number,
              game.currentTurn.currentPlayer,
              Phase.Playing
            ),
            isOver = game.isOver
          )
      )

  case Build(val spot: Spot, val buildingType: BuildingType)
      extends Action(
        ActionType.Build,
        game => game
      )

  case BuyDevelopmentCard
      extends Action(
        ActionType.BuyDevelopmentCard,
        game => game
      )

  case PlayDevelopmentCard(val developmentCard: DevelopmentCard)
      extends Action(
        ActionType.PlayDevelopmentCard,
        game =>
          Game(
            players = game.players,
            currentTurn = Turn(
              game.currentTurn.number,
              game.currentTurn.currentPlayer,
              Phase.Playing
            ),
            isOver = game.isOver
          )
      )

  case Trade
      extends Action(
        ActionType.Trade,
        game => game
      )

  case NextTurn
      extends Action(
        ActionType.NextTurn,
        game =>
          val nextPlayer = game.players(game.currentTurn.number % game.players.size)
          Game(
            players = game.players,
            currentTurn = Turn(game.currentTurn.number + 1, nextPlayer),
            isOver = game.isOver
          )
      )
