package scatan.model

import scatan.model.Spot
import scatan.model.map.Hexagon
import scala.util.Random
import game.{Player, Turn, Game, Action}

/** The type of action that can be performed in a turn.
  */
enum ActionsType:
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
enum Actions(actionType: ActionsType, apply: Game[ActionsType] => Game[ActionsType]) extends Action(actionType, apply):

  case Roll(diceResult: Int = Actions.rollDice)
      extends Actions(
        ActionsType.Roll,
        Actions.RollAction(diceResult)
      )

  case PlaceRobber(val hexagon: Hexagon)
      extends Actions(
        ActionsType.PlaceRobber,
        Actions.PlaceRobberAction(hexagon)
      )

  case StoleCard(val player: Player)
      extends Actions(
        ActionsType.StoleCard,
        Actions.StoleCardAction(player)
      )

  case Build(val spot: Spot, val buildingType: BuildingType)
      extends Actions(
        ActionsType.Build,
        Actions.BuildAction(spot, buildingType)
      )

  case BuyDevelopmentCard
      extends Actions(
        ActionsType.BuyDevelopmentCard,
        Actions.BuyDevelopmentCardAction
      )

  case PlayDevelopmentCard(val developmentCard: DevelopmentCard)
      extends Actions(
        ActionsType.PlayDevelopmentCard,
        Actions.PlayDevelopmentCardAction(developmentCard)
      )

  case Trade
      extends Actions(
        ActionsType.Trade,
        Actions.TradeAction
      )

  case NextTurn
      extends Actions(
        ActionsType.NextTurn,
        Actions.NextTurnAction
      )

object Actions:

  private[this] def rollDice: Int = (1 + Random.nextInt(6)) + (1 + Random.nextInt(6))

  private[this] def RollAction(diceResult: Int)(game: Game[ActionsType]): Game[ActionsType] =
    if diceResult == 7 then
      Game(
        players = game.players,
        currentTurn = Turn(
          game.currentTurn.number,
          game.currentTurn.currentPlayer,
          Phases.PlaceRobber
        ),
        isOver = game.isOver
      )
    else
      Game(
        players = game.players,
        currentTurn = Turn(
          game.currentTurn.number,
          game.currentTurn.currentPlayer,
          Phases.Playing
        ),
        isOver = game.isOver
      )

  private[this] def PlaceRobberAction(hexagon: Hexagon)(game: Game[ActionsType]): Game[ActionsType] = identity(game)

  private[this] def StoleCardAction(player: Player)(game: Game[ActionsType]): Game[ActionsType] = identity(game)

  private[this] def BuildAction(spot: Spot, buildingType: BuildingType)(game: Game[ActionsType]): Game[ActionsType] =
    identity(game)

  private[this] def BuyDevelopmentCardAction(game: Game[ActionsType]): Game[ActionsType] = identity(game)

  private[this] def PlayDevelopmentCardAction(developmentCard: DevelopmentCard)(
      game: Game[ActionsType]
  ): Game[ActionsType] = identity(game)

  private[this] def TradeAction(game: Game[ActionsType]): Game[ActionsType] = identity(game)

  private[this] def NextTurnAction(game: Game[ActionsType]): Game[ActionsType] =
    val nextPlayer = game.players(game.currentTurn.number % game.players.size)
    Game(
      players = game.players,
      currentTurn = Turn[ActionsType](game.currentTurn.number + 1, nextPlayer, Phases.Initial),
      isOver = game.isOver
    )
