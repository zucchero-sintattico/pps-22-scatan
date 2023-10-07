package scatan.model.game.config

import scatan.BaseTest

class ScatanStepsTest extends BaseTest:

  "A Scatan Step" should "be Setup Settlement" in {
    ScatanSteps.SetupSettlement
  }

  it should "be Setup Road" in {
    ScatanSteps.SetupRoad
  }

  it should "be Setupped" in {
    ScatanSteps.Setupped
  }

  it should "be Starting" in {
    ScatanSteps.Starting
  }

  it should "be Place Robber" in {
    ScatanSteps.PlaceRobber
  }

  it should "be Steal Card" in {
    ScatanSteps.StealCard
  }

  it should "be Playing" in {
    ScatanSteps.Playing
  }
