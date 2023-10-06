package scatan.model.game

import org.scalatest.matchers.should.Matchers.shouldBe
import scatan.BaseTest
import scatan.model.game.config.{ScatanPhases, ScatanPlayer}

class ScatanRulesTest extends BaseTest:

  val rules = ScatanDSL.rules

  "The rules" should "be valid" in {
    rules.valid should be(true)
  }

  it should "allow 3 or 4 players" in {
    rules.allowedPlayersSizes should be(Set(3, 4))
  }

  it should "start with a Scatan State" in {
    val players = Seq(ScatanPlayer("a"), ScatanPlayer("b"), ScatanPlayer("c"))
    val initialState = rules.startingStateFactory(players)
    initialState should be(ScatanState(players))
  }

  it should "start with Setup phase" in {
    rules.startingPhase shouldBe ScatanPhases.Setup
  }
