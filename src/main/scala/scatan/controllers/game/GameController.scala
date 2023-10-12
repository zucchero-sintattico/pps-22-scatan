package scatan.controllers.game

import scatan.lib.mvc.{BaseController, Controller}
import scatan.model.ApplicationState
import scatan.model.components.BuildingType
import scatan.model.game.ScatanModelOps.{onError, updateGame}
import scatan.model.game.config.ScatanPhases.{Game, Setup}
import scatan.model.game.config.ScatanPlayer
import scatan.model.map.{RoadSpot, StructureSpot}
import scatan.views.game.GameView
import scatan.views.game.components.CardContextMap.CardType
import scatan.model.map.Hexagon

trait GameController extends Controller[ApplicationState]:
  def onRoadSpot(spot: RoadSpot): Unit
  def onStructureSpot(spot: StructureSpot): Unit
  def nextTurn(): Unit
  def rollDice(): Unit
  def clickCard(card: CardType): Unit
  def placeRobber(hexagon: Hexagon): Unit
  def stealCard(player: ScatanPlayer): Unit
  def buyDevelopmentCard(): Unit

object GameController:
  def apply(requirements: Controller.Requirements[GameView, ApplicationState]): GameController =
    GameControllerImpl(requirements)

private class GameControllerImpl(requirements: Controller.Requirements[GameView, ApplicationState])
    extends BaseController(requirements)
    with GameController:

  override def placeRobber(hexagon: Hexagon): Unit =
    this.model
      .updateGame(_.placeRobber(hexagon))
      .onError(view.displayMessage("Cannot place robber"))

  override def clickCard(card: CardType): Unit = ???

  override def nextTurn(): Unit = this.model.updateGame(_.nextTurn)

  override def rollDice(): Unit =
    this.model.updateGame(_.rollDice(diceResult => this.view.displayMessage(s"Roll dice result: $diceResult")));

  override def onRoadSpot(spot: RoadSpot): Unit =
    val phase = this.model.state.game.map(_.gameStatus.phase).get
    phase match
      case Setup =>
        this.model
          .updateGame(_.assignRoad(spot))
          .onError(view.displayMessage("Cannot assign road here"))
      case Game =>
        this.model
          .updateGame(_.buildRoad(spot))
          .onError(view.displayMessage("Cannot build road here"))

  override def onStructureSpot(spot: StructureSpot): Unit =
    val phase = this.model.state.game.map(_.gameStatus.phase).get
    phase match
      case Setup =>
        this.model
          .updateGame(_.assignSettlement(spot))
          .onError(view.displayMessage("Cannot assign settlement here"))
      case Game =>
        val alreadyContainsSettlement = this.model.state.game
          .map(_.state)
          .map(_.assignedBuildings)
          .flatMap(_.get(spot))
          .exists(_.buildingType == BuildingType.Settlement)
        if alreadyContainsSettlement then
          this.model
            .updateGame(_.buildCity(spot))
            .onError(view.displayMessage("Cannot build city here"))
        else
          this.model
            .updateGame(_.buildSettlement(spot))
            .onError(view.displayMessage("Cannot build settlement here"))

  override def stealCard(player: ScatanPlayer): Unit =
    this.model
      .updateGame(_.stealCard(player))
      .onError(view.displayMessage("Cannot steal card"))

  override def buyDevelopmentCard(): Unit =
    this.model
      .updateGame(_.buyDevelopmentCard)
      .onError(view.displayMessage("Cannot buy development card"))
