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
    Phase.Initial.allowedActions shouldBe Set(Action.Roll, Action.RollSeven)
  }

  it should "have allowed actions for PlaceRobber" in {
    Phase.PlaceRobber.allowedActions shouldBe Set(Action.PlaceRobber)
  }

  it should "have allowed actions for StoleCard" in {
    Phase.StoleCard.allowedActions shouldBe Set(Action.StoleCard)
  }

  it should "have allowed actions for Playing" in {
    Phase.Playing.allowedActions shouldBe Set(
      Action.Build,
      Action.BuyDevelopmentCard,
      Action.PlayDevelopmentCard,
      Action.Trade,
      Action.NextTurn
    )
  }

  it should "have isAllowed" in {
    Phase.Initial.isAllowed(Action.Roll) shouldBe true
  }

  it should "have a nextPhaseWhen" in {
    Phase.Initial.nextPhaseWhen(Action.Roll) shouldBe Some(Phase.Playing)
  }

  it should "have a nextPhaseWhen for PlaceRobber" in {
    Phase.PlaceRobber.nextPhaseWhen(Action.PlaceRobber) shouldBe Some(Phase.StoleCard)
  }

  it should "have a nextPhaseWhen for StoleCard" in {
    Phase.StoleCard.nextPhaseWhen(Action.StoleCard) shouldBe Some(Phase.Playing)
  }

  it should "have a nextPhaseWhen for Playing" in {
    Phase.Playing.nextPhaseWhen(Action.NextTurn) shouldBe Some(Phase.Initial)
    Phase.Playing.nextPhaseWhen(Action.Build) shouldBe Some(Phase.Playing)
    Phase.Playing.nextPhaseWhen(Action.BuyDevelopmentCard) shouldBe Some(Phase.Playing)
    Phase.Playing.nextPhaseWhen(Action.PlayDevelopmentCard) shouldBe Some(Phase.Playing)
    Phase.Playing.nextPhaseWhen(Action.Trade) shouldBe Some(Phase.Playing)
  }