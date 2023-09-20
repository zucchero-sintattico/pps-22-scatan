package scatan.model

import scatan.BaseTest

class PhaseTest extends BaseTest:
  "A Phase" should "be initial" in {
    Phase.Initial shouldBe Phase.Initial
  }

  it should "be PlaceRobber" in {
    Phase.PlaceRobber shouldBe Phase.PlaceRobber
  }

  it should "be StoleCard" in {
    Phase.StoleCard shouldBe Phase.StoleCard
  }

  it should "be Playing" in {
    Phase.Playing shouldBe Phase.Playing
  }

  it should "have allowed actions" in {
    Phase.Initial.allowedActions shouldBe Set(ActionType.Roll)
  }

  it should "have allowed actions for PlaceRobber" in {
    Phase.PlaceRobber.allowedActions shouldBe Set(ActionType.PlaceRobber)
  }

  it should "have allowed actions for StoleCard" in {
    Phase.StoleCard.allowedActions shouldBe Set(ActionType.StoleCard)
  }

  it should "have allowed actions for Playing" in {
    Phase.Playing.allowedActions shouldBe Set(
      ActionType.Build,
      ActionType.BuyDevelopmentCard,
      ActionType.PlayDevelopmentCard,
      ActionType.Trade,
      ActionType.NextTurn
    )
  }

  it should "have isAllowed" in {
    Phase.Initial.isAllowed(ActionType.Roll) shouldBe true
  }
