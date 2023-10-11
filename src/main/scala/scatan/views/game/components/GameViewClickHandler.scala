package scatan.views.game.components

import com.raquo.airstream.core.Signal
import scatan.controllers.game.GameController
import scatan.model.ApplicationState
import scatan.model.components.BuildingType
import scatan.model.game.{ScatanGame, ScatanState}
import scatan.model.game.config.ScatanPhases.{Game, Setup}
import scatan.model.map.{Hexagon, RoadSpot, StructureSpot}
import scatan.views.game.components.CardContextMap.CardType

trait GameViewClickHandler:
  def onRoadClick(roadSpot: RoadSpot): Unit
  def onStructureClick(structureSpot: StructureSpot): Unit
  def onHexagonClick(hexagon: Hexagon): Unit

  def onRollDiceClick(): Unit
  def onBuyDevelopmentCardClick(): Unit
  def onEndTurnClick(): Unit

  def onCardClick(cardType: CardType): Unit

object GameViewClickHandler:
  def apply(gameController: GameController): GameViewClickHandler =
    new GameViewClickHandler:

      def state: ApplicationState = gameController.state
      def game: ScatanGame = state.game.get

      override def onRoadClick(roadSpot: RoadSpot): Unit =
        game.gameStatus.phase match
          case Setup =>
            gameController.assignRoad(roadSpot)
          case Game =>
            gameController.buildRoad(roadSpot)

      override def onStructureClick(structureSpot: StructureSpot): Unit =
        game.gameStatus.phase match
          case Setup =>
            gameController.assignSettlement(structureSpot)
          case Game =>
            val alreadyContainsSettlement =
              game.state.assignedBuildings
                .get(structureSpot)
                .exists(_.buildingType == BuildingType.Settlement)
            if alreadyContainsSettlement then gameController.buildCity(structureSpot)
            else gameController.buildSettlement(structureSpot)

      override def onHexagonClick(hexagon: Hexagon): Unit =
        gameController.placeRobber(hexagon)

      override def onRollDiceClick(): Unit =
        gameController.rollDice()

      override def onBuyDevelopmentCardClick(): Unit =
        gameController.buyDevelopmentCard()

      override def onEndTurnClick(): Unit =
        gameController.nextTurn()

      override def onCardClick(cardType: CardType): Unit =
        ???
