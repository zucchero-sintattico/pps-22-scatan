package scatan.model.game.config

import scatan.BaseTest

class ScatanActionsTest extends BaseTest:

  "A Scatan Action" should "be roll" in {
    ScatanActions.RollDice
  }

  it should "be place robber" in {
    ScatanActions.PlaceRobber
  }

  it should "be stole card" in {
    ScatanActions.StealCard
  }

  it should "be build road" in {
    ScatanActions.BuildRoad
  }

  it should "be build settlement" in {
    ScatanActions.BuildSettlement
  }

  it should "be build city" in {
    ScatanActions.BuildCity
  }

  it should "be buy development card" in {
    ScatanActions.BuyDevelopmentCard
  }

  it should "be play development card" in {
    ScatanActions.PlayDevelopmentCard
  }

  it should "be trade with bank" in {
    ScatanActions.TradeWithBank
  }

  it should "be trade with player" in {
    ScatanActions.TradeWithPlayer
  }

  it should "be assign settlement" in {
    ScatanActions.AssignSettlement
  }

  it should "be assign road" in {
    ScatanActions.AssignRoad
  }

  it should "be next turn" in {
    ScatanActions.NextTurn
  }
