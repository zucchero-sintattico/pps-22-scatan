package scatan.model.game

import org.scalatest.matchers.should.Matchers.shouldBe
import scatan.BaseTest
import scatan.lib.game.GameStatus
import scatan.model.game.config.{ScatanActions, ScatanPhases, ScatanPlayer, ScatanSteps}

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

  it should "change turn on Changing Turn step when in Setup phase" in {
    rules.endingSteps.get(ScatanPhases.Setup) shouldBe Some(ScatanSteps.ChangingTurn)
  }

  it should "change turn on Changing Turn step when in Game phase" in {
    rules.endingSteps.get(ScatanPhases.Game) shouldBe Some(ScatanSteps.ChangingTurn)
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

  it should "only allow to assign a settlement when in Setup phase and SetupSettlement step" in {
    val status = GameStatus(ScatanPhases.Setup, ScatanSteps.SetupSettlement)
    rules.allowedActions(status) shouldBe Set(ScatanActions.AssignSettlement)
  }

  it should "go in Setup Road step when Assigning a settlement in Setup phase and SetupSettlement step" in {
    val status = GameStatus(ScatanPhases.Setup, ScatanSteps.SetupSettlement)
    rules.nextSteps((status, ScatanActions.AssignSettlement)) shouldBe ScatanSteps.SetupRoad
  }

  it should "only allow to assign a road going in Setupped step when in Setup phase and SetupRoad step" in {
    val status = GameStatus(ScatanPhases.Setup, ScatanSteps.SetupRoad)
    rules.allowedActions(status) shouldBe Set(ScatanActions.AssignRoad)
  }

  it should "go in Changing Turn step when Assigning a road in Setup phase and SetupRoad step" in {
    val status = GameStatus(ScatanPhases.Setup, ScatanSteps.SetupRoad)
    rules.nextSteps((status, ScatanActions.AssignRoad)) shouldBe ScatanSteps.ChangingTurn
  }

  it should "allow to roll dice and play a development card when in Game phase and Starting step" in {
    val status = GameStatus(ScatanPhases.Game, ScatanSteps.Starting)
    rules.allowedActions(status) shouldBe Set(
      ScatanActions.RollDice,
      ScatanActions.RollSeven,
      ScatanActions.PlayDevelopmentCard
    )
  }

  it should "go again in Starting step when playing a development card in Game phase and Starting step" in {
    val status = GameStatus(ScatanPhases.Game, ScatanSteps.Starting)
    rules.nextSteps((status, ScatanActions.PlayDevelopmentCard)) shouldBe ScatanSteps.Starting
  }

  it should "go in Playing step when rolling dice in Game phase and Starting step" in {
    val status = GameStatus(ScatanPhases.Game, ScatanSteps.Starting)
    rules.nextSteps((status, ScatanActions.RollDice)) shouldBe ScatanSteps.Playing
  }

  it should "go in Place Robber step when rolling seven in Game phase and Starting step" in {
    val status = GameStatus(ScatanPhases.Game, ScatanSteps.Starting)
    rules.nextSteps((status, ScatanActions.RollSeven)) shouldBe ScatanSteps.PlaceRobber
  }

  it should "only allow to place robber when in Game phase and PlaceRobber step" in {
    val status = GameStatus(ScatanPhases.Game, ScatanSteps.PlaceRobber)
    rules.allowedActions(status) shouldBe Set(ScatanActions.PlaceRobber)
  }

  it should "go in Steal Card step when placing robber in Game phase and PlaceRobber step" in {
    val status = GameStatus(ScatanPhases.Game, ScatanSteps.PlaceRobber)
    rules.nextSteps((status, ScatanActions.PlaceRobber)) shouldBe ScatanSteps.StealCard
  }

  it should "only allow to steal card when in Game phase and StealCard step" in {
    val status = GameStatus(ScatanPhases.Game, ScatanSteps.StealCard)
    rules.allowedActions(status) shouldBe Set(ScatanActions.StoleCard)
  }

  it should "go in Playing step when stealing card in Game phase and StealCard step" in {
    val status = GameStatus(ScatanPhases.Game, ScatanSteps.StealCard)
    rules.nextSteps((status, ScatanActions.StoleCard)) shouldBe ScatanSteps.Playing
  }

  it should "remain in Playing step when in Game phase and Playing step except for Next Turn" in {
    val status = GameStatus(ScatanPhases.Game, ScatanSteps.Playing)
    rules.allowedActions(status) shouldBe Set(
      ScatanActions.BuildSettlement,
      ScatanActions.BuildCity,
      ScatanActions.BuildRoad,
      ScatanActions.BuyDevelopmentCard,
      ScatanActions.PlayDevelopmentCard,
      ScatanActions.TradeWithBank,
      ScatanActions.TradeWithPlayer,
      ScatanActions.NextTurn
    )
    for action <- rules.allowedActions(status).filter(_ != ScatanActions.NextTurn)
    do rules.nextSteps((status, action)) shouldBe ScatanSteps.Playing
    rules.nextSteps((status, ScatanActions.NextTurn)) shouldBe ScatanSteps.ChangingTurn
  }
