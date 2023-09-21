package scatan.model

import scatan.model.Spot
import scatan.model.map.Hexagon
import scala.util.Random

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
  case Roll(diceResult: Int = Action.rollDice)
      extends Action(
        ActionType.Roll,
        Action.RollAction(diceResult)
      )

  case PlaceRobber(val hexagon: Hexagon)
      extends Action(
        ActionType.PlaceRobber,
        Action.PlaceRobberAction(hexagon)
      )

  case StoleCard(val player: Player)
      extends Action(
        ActionType.StoleCard,
        Action.StoleCardAction(player)
      )

  case Build(val spot: Spot, val buildingType: BuildingType)
      extends Action(
        ActionType.Build,
        Action.BuildAction(spot, buildingType)
      )

  case BuyDevelopmentCard
      extends Action(
        ActionType.BuyDevelopmentCard,
        Action.BuyDevelopmentCardAction
      )

  case PlayDevelopmentCard(val developmentCard: DevelopmentCard)
      extends Action(
        ActionType.PlayDevelopmentCard,
        Action.PlayDevelopmentCardAction(developmentCard)
      )

  case Trade
      extends Action(
        ActionType.Trade,
        Action.TradeAction
      )

  case NextTurn
      extends Action(
        ActionType.NextTurn,
        Action.NextTurnAction
      )

object Action:

  private[this] def rollDice: Int = (1 + Random.nextInt(6)) + (1 + Random.nextInt(6))

  private[this] def RollAction(diceResult: Int)(game: Game): Game =
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

  private[this] def PlaceRobberAction(hexagon: Hexagon)(game: Game): Game = identity(game)

  private[this] def StoleCardAction(player: Player)(game: Game): Game =
    Game(
      players = game.players,
      currentTurn = Turn(
        game.currentTurn.number,
        game.currentTurn.currentPlayer,
        Phase.Playing
      ),
      isOver = game.isOver
    )

  private[this] def BuildAction(spot: Spot, buildingType: BuildingType)(game: Game): Game = identity(game)

  private[this] def BuyDevelopmentCardAction(game: Game): Game = identity(game)

  private[this] def PlayDevelopmentCardAction(developmentCard: DevelopmentCard)(game: Game): Game = identity(game)

  private[this] def TradeAction(game: Game): Game = identity(game)

  private[this] def NextTurnAction(game: Game): Game =
    val nextPlayer = game.players(game.currentTurn.number % game.players.size)
    Game(
      players = game.players,
      currentTurn = Turn(game.currentTurn.number + 1, nextPlayer),
      isOver = game.isOver
    )
