package scatan.model

import scatan.BaseTest

class ActionTypeTest extends BaseTest:

  "An Action Type" should "be roll" in {
    ActionsType.Roll shouldBe ActionsType.Roll
  }

  it should "be build" in {
    ActionsType.Build shouldBe ActionsType.Build
  }

  it should "be buy development card" in {
    ActionsType.BuyDevelopmentCard shouldBe ActionsType.BuyDevelopmentCard
  }

  it should "be play development card" in {
    ActionsType.PlayDevelopmentCard shouldBe ActionsType.PlayDevelopmentCard
  }

  it should "be trade" in {
    ActionsType.Trade shouldBe ActionsType.Trade
  }

  it should "be next turn" in {
    ActionsType.NextTurn shouldBe ActionsType.NextTurn
  }
