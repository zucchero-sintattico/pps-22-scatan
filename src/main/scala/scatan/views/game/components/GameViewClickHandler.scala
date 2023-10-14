package scatan.views.game.components

import com.raquo.airstream.core.Signal
import scatan.controllers.game.GameController
import scatan.model.ApplicationState
import scatan.model.components.*
import scatan.model.components.DevelopmentType.{Knight, Monopoly, RoadBuilding, YearOfPlenty}
import scatan.model.game.ScatanGame
import scatan.model.game.config.ScatanPhases.{Game, Setup}
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.state.ScatanState
import scatan.model.map.{Hexagon, RoadSpot, StructureSpot}
import scatan.views.game.GameView
import scatan.views.game.components.CardContextMap.CardType

trait GameViewClickHandler:
  /** Handles a click on a road spot.
    * @param roadSpot
    *   the road spot that was clicked
    */
  def onRoadClick(roadSpot: RoadSpot): Unit

  /** Handles a click on a structure spot.
    * @param roadSpot
    *   the structure spot that was clicked
    */
  def onStructureClick(structureSpot: StructureSpot): Unit

  /** Handles a click on an hexagon.
    * @param roadSpot
    *   the hexagon that was clicked
    */
  def onHexagonClick(hexagon: Hexagon): Unit

  /** Handles a click on the roll dice button.
    */
  def onRollDiceClick(): Unit

  /** Handles a click on the buy development card button.
    */
  def onBuyDevelopmentCardClick(): Unit

  /** Handles a click on the end turn button.
    */
  def onEndTurnClick(): Unit

  /** Handles a click on the steal card button.
    * @param player
    *   the player to steal a card from
    */
  def onStealCardClick(player: ScatanPlayer): Unit

  /** Handles a click on a card.
    * @param cardType
    *   the card that was clicked
    */
  def onCardClick(cardType: CardType): Unit

  /** Handles a click on the trade with bank button.
    * @param offer
    *   the resource type to offer
    * @param request
    *   the resource type to request
    */
  def onTradeWithBank(offer: ResourceType, request: ResourceType): Unit

  /** Handles a click on the trade with player button.
    * @param receiver
    *   the player to trade with
    * @param offer
    *   the resource type to offer
    * @param request
    *   the resource type to request
    */
  def onTradeWithPlayer(
      receiver: ScatanPlayer,
      offer: Map[ResourceType, Int],
      request: Map[ResourceType, Int]
  ): Unit

object GameViewClickHandler:
  def apply(view: GameView, gameController: GameController): GameViewClickHandler =
    new GameViewClickHandler:

      def state: ApplicationState = gameController.state
      def game: ScatanGame = state.game.get

      var playingKnight = false
      var playingRoadBuilding = false
      var roadBuildingRoads: Seq[RoadSpot] = Seq.empty

      override def onRoadClick(roadSpot: RoadSpot): Unit =
        if playingRoadBuilding then
          roadBuildingRoads = roadBuildingRoads :+ roadSpot
          if roadBuildingRoads.sizeIs == 2 then
            playingRoadBuilding = false
            gameController.playRoadBuildingDevelopment(roadBuildingRoads.head, roadBuildingRoads.last)
            roadBuildingRoads = Seq.empty
          else view.displayMessage("Select another road")
        else
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
        if playingKnight then
          playingKnight = false
          gameController.playKnightDevelopment(hexagon)
        else gameController.placeRobber(hexagon)

      override def onRollDiceClick(): Unit =
        gameController.rollDice()

      override def onBuyDevelopmentCardClick(): Unit =
        gameController.buyDevelopmentCard()

      override def onEndTurnClick(): Unit =
        gameController.nextTurn()

      override def onStealCardClick(player: ScatanPlayer): Unit =
        gameController.stealCard(player)

      override def onCardClick(cardType: CardType): Unit =
        cardType match
          case development: DevelopmentType =>
            if state.game.exists(_.canPlayDevelopment(development)) then
              development match
                case Knight =>
                  playingKnight = true
                  view.displayMessage("Select a hexagon to place the robber")
                case YearOfPlenty =>
                  DevelopmentCardPopups.FirstYearOfPlentyCardPopup.show { first =>
                    DevelopmentCardPopups.SecondYearOfPlentyCardPopup.show { second =>
                      gameController.playYearOfPlentyDevelopment(first, second)
                    }
                  }
                case Monopoly =>
                  DevelopmentCardPopups.MonopolyCardPopup.show { resource =>
                    gameController.playMonopolyDevelopment(resource)
                  }
                case RoadBuilding =>
                  view.displayMessage("Select two roads to build")
                  playingRoadBuilding = true
                case _ => ()
          case _ => ()

      override def onTradeWithBank(offer: ResourceType, request: ResourceType): Unit =
        gameController.tradeWithBank(offer, request)

      override def onTradeWithPlayer(
          receiver: ScatanPlayer,
          offer: Map[ResourceType, Int],
          request: Map[ResourceType, Int]
      ): Unit =
        val offerCards = offer.flatMap((resourceType, amount) => ResourceCard(resourceType) ** amount).toSeq
        val requestCards = request.flatMap((resourceType, amount) => ResourceCard(resourceType) ** amount).toSeq
        gameController.tradeWithPlayer(receiver, offerCards, requestCards)
