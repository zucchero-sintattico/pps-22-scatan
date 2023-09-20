package scatan.model

import scatan.BaseTest

class ActionTest extends BaseTest:
  "An Action" should "be roll" in {
    Action.Roll shouldBe Action.Roll
  }

  it should "be roll seven" in {
    Action.RollSeven shouldBe Action.RollSeven
  }

  it should "be build" in {
    Action.Build shouldBe Action.Build
  }

  it should "be buy development card" in {
    Action.BuyDevelopmentCard shouldBe Action.BuyDevelopmentCard
  }

  it should "be play development card" in {
    Action.PlayDevelopmentCard shouldBe Action.PlayDevelopmentCard
  }

  it should "be trade" in {
    Action.Trade shouldBe Action.Trade
  }

  it should "be next turn" in {
    Action.NextTurn shouldBe Action.NextTurn
  }