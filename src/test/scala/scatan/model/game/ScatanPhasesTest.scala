package scatan.model.game

import scatan.BaseTest
import scatan.model.game.ScatanPhases

class ScatanPhasesTest extends BaseTest:

  "A Scatan Phase" should "be initial" in {
    ScatanPhases.Initial
  }

  it should "be robber placement" in {
    ScatanPhases.RobberPlacement
  }

  it should "be card stealing" in {
    ScatanPhases.CardStealing
  }

  it should "be playing" in {
    ScatanPhases.Playing
  }
