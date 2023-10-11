package scatan.controllers.game

import scatan.lib.mvc.{BaseController, Controller}
import scatan.model.ApplicationState
import scatan.model.components.ResourceType
import scatan.model.game.ScatanModelOps.{onError, updateGame}
import scatan.model.game.config.ScatanPhases
import scatan.model.map.{RoadSpot, StructureSpot}
import scatan.views.game.GameView
import scatan.model.map.Hexagon

trait GameController extends Controller[ApplicationState]:
  def state: ApplicationState

  def assignRoad(spot: RoadSpot): Unit
  def assignSettlement(spot: StructureSpot): Unit
  def buildRoad(spot: RoadSpot): Unit
  def buildSettlement(spot: StructureSpot): Unit
  def buildCity(spot: StructureSpot): Unit
  def placeRobber(hexagon: Hexagon): Unit

  def nextTurn(): Unit
  def rollDice(): Unit
  def buyDevelopmentCard(): Unit

  def playKnightDevelopment(robberPosition: Hexagon): Unit
  def playYearOfPlentyDevelopment(resource1: ResourceType, resource2: ResourceType): Unit
  def playMonopolyDevelopment(resource: ResourceType): Unit
  def playRoadBuildingDevelopment(spot1: RoadSpot, spot2: RoadSpot): Unit

object GameController:
  def apply(requirements: Controller.Requirements[GameView, ApplicationState]): GameController =
    GameControllerImpl(requirements)

private class GameControllerImpl(requirements: Controller.Requirements[GameView, ApplicationState])
    extends BaseController(requirements)
    with GameController:

  override def state: ApplicationState = this.model.state

  override def assignRoad(spot: RoadSpot): Unit =
    this.model
      .updateGame(_.assignRoad(spot))
      .onError(view.displayMessage("Cannot assign road here"))

  override def assignSettlement(spot: StructureSpot): Unit =
    this.model
      .updateGame(_.assignSettlement(spot))
      .onError(view.displayMessage("Cannot assign settlement here"))

  override def buildRoad(spot: RoadSpot): Unit =
    this.model
      .updateGame(_.buildRoad(spot))
      .onError(view.displayMessage("Cannot build road here"))

  override def buildSettlement(spot: StructureSpot): Unit =
    this.model
      .updateGame(_.buildSettlement(spot))
      .onError(view.displayMessage("Cannot build settlement here"))

  override def buildCity(spot: StructureSpot): Unit =
    this.model
      .updateGame(_.buildCity(spot))
      .onError(view.displayMessage("Cannot build city here"))

  override def placeRobber(hexagon: Hexagon): Unit =
    this.model
      .updateGame(_.placeRobber(hexagon))
      .onError(view.displayMessage("Cannot place robber"))

  override def nextTurn(): Unit =
    this.model.updateGame(_.nextTurn)

  override def rollDice(): Unit =
    this.model
      .updateGame(_.rollDice(diceResult => this.view.displayMessage(s"Roll dice result: $diceResult")))
      .onError(view.displayMessage("Cannot roll dice"))

  override def buyDevelopmentCard(): Unit =
    this.model
      .updateGame(_.buyDevelopmentCard)
      .onError(view.displayMessage("Cannot buy development card"))

  override def playKnightDevelopment(robberPosition: Hexagon): Unit =
    this.model
      .updateGame(_.playKnightDevelopment(robberPosition))
      .onError(view.displayMessage("Cannot play knight development card"))

  override def playYearOfPlentyDevelopment(resource1: ResourceType, resource2: ResourceType): Unit =
    this.model
      .updateGame(_.playYearOfPlentyDevelopment(resource1, resource2))
      .onError(view.displayMessage("Cannot play year of plenty development card"))

  override def playMonopolyDevelopment(resource: ResourceType): Unit =
    this.model
      .updateGame(_.playMonopolyDevelopment(resource))
      .onError(view.displayMessage("Cannot play monopoly development card"))

  override def playRoadBuildingDevelopment(spot1: RoadSpot, spot2: RoadSpot): Unit =
    this.model
      .updateGame(_.playRoadBuildingDevelopment(spot1, spot2))
      .onError(view.displayMessage("Cannot play road building development card"))