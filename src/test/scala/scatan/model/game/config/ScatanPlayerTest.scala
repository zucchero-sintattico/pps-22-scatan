package scatan.model.game.config

import scatan.BaseTest

class ScatanPlayerTest extends BaseTest:

  "A Scatan Player" should "exists" in {
    ScatanPlayer
  }

  it should "have a name" in {
    val player = ScatanPlayer("name")
    player.name should be("name")
  }
