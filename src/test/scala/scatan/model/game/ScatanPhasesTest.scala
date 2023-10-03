package scatan.model.game

import scatan.BaseTest
import scatan.model.game.ScatanPhases

class ScatanPhasesTest extends BaseTest:

  "A Scatan Phase" should "be setup" in {
    ScatanPhases.Setup
  }

  it should "be game" in {
    ScatanPhases.Game
  }
