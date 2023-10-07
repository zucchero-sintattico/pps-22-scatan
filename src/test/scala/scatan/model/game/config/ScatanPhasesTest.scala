package scatan.model.game.config

import scatan.BaseTest

class ScatanPhasesTest extends BaseTest:

  "A Scatan Phase" should "be Setup" in {
    ScatanPhases.Setup
  }

  it should "be Game" in {
    ScatanPhases.Game
  }
