package scatan.model.game

import org.scalatest.matchers.should.Matchers.shouldBe
import scatan.BaseTest
import scatan.model.game.config.{ScatanPhases, ScatanPlayer, ScatanSteps}

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

  it should "start in SetupSettlement step when in Setup phase" in {
    rules.startingSteps.get(ScatanPhases.Setup) shouldBe Some(ScatanSteps.SetupSettlement)
  }

  it should "start in Starting step when in Game phase" in {
    rules.startingSteps.get(ScatanPhases.Game) shouldBe Some(ScatanSteps.Starting)
  }

  it should "allow to change turn on Setupped step when in Setup phase" in {
    rules.endingSteps.get(ScatanPhases.Setup) shouldBe Some(ScatanSteps.Setupped)
  }

  it should "allow to change turn on Playing step when in Game phase" in {
    rules.endingSteps.get(ScatanPhases.Game) shouldBe Some(ScatanSteps.Playing)
  }

  it should "specify that the next phase after Setup is Game" in {
    rules.nextPhase.get(ScatanPhases.Setup) shouldBe Some(ScatanPhases.Game)
  }

  it should "iterate players in circularWithBack order when in Setup phase" in {
    val players = Seq(ScatanPlayer("a"), ScatanPlayer("b"), ScatanPlayer("c"))
    val iterator = rules.phaseTurnIteratorFactories(ScatanPhases.Setup)(players)
    iterator.next() shouldBe ScatanPlayer("a")
    iterator.next() shouldBe ScatanPlayer("b")
    iterator.next() shouldBe ScatanPlayer("c")
    iterator.next() shouldBe ScatanPlayer("c")
    iterator.next() shouldBe ScatanPlayer("b")
    iterator.next() shouldBe ScatanPlayer("a")
    iterator.hasNext shouldBe false
  }

  it should "iterate players in normal order when in Game phase" in {
    val players = Seq(ScatanPlayer("a"), ScatanPlayer("b"), ScatanPlayer("c"))
    val iterator = rules.phaseTurnIteratorFactories(ScatanPhases.Game)(players)
    for _ <- 1 to 10
    do
      iterator.next() shouldBe ScatanPlayer("a")
      iterator.next() shouldBe ScatanPlayer("b")
      iterator.next() shouldBe ScatanPlayer("c")
    iterator.hasNext shouldBe true
  }
