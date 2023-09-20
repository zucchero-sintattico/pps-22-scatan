package scatan.model

import scatan.BaseTest

class ActionTypeTest extends BaseTest:

  "An Action Type" should "be roll" in {
    ActionType.Roll shouldBe ActionType.Roll
  }

  it should "be build" in {
    ActionType.Build shouldBe ActionType.Build
  }

  it should "be buy development card" in {
    ActionType.BuyDevelopmentCard shouldBe ActionType.BuyDevelopmentCard
  }

  it should "be play development card" in {
    ActionType.PlayDevelopmentCard shouldBe ActionType.PlayDevelopmentCard
  }

  it should "be trade" in {
    ActionType.Trade shouldBe ActionType.Trade
  }

  it should "be next turn" in {
    ActionType.NextTurn shouldBe ActionType.NextTurn
  }
