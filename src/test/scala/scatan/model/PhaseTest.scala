package scatan.model

import scatan.BaseTest

class PhasesTest extends BaseTest:
  "A Phase" should "be initial" in {
    Phases.Initial shouldBe Phases.Initial
  }

  it should "be PlaceRobber" in {
    Phases.PlaceRobber shouldBe Phases.PlaceRobber
  }

  it should "be StoleCard" in {
    Phases.StoleCard shouldBe Phases.StoleCard
  }

  it should "be Playing" in {
    Phases.Playing shouldBe Phases.Playing
  }

  it should "have allowed actions" in {
    Phases.Initial.allowedActions shouldBe Set(ActionsType.Roll)
  }

  it should "have allowed actions for PlaceRobber" in {
    Phases.PlaceRobber.allowedActions shouldBe Set(ActionsType.PlaceRobber)
  }

  it should "have allowed actions for StoleCard" in {
    Phases.StoleCard.allowedActions shouldBe Set(ActionsType.StoleCard)
  }

  it should "have allowed actions for Playing" in {
    Phases.Playing.allowedActions shouldBe Set(
      ActionsType.Build,
      ActionsType.BuyDevelopmentCard,
      ActionsType.PlayDevelopmentCard,
      ActionsType.Trade,
      ActionsType.NextTurn
    )
  }

  it should "have isAllowed" in {
    Phases.Initial.isAllowed(ActionsType.Roll) shouldBe true
  }
