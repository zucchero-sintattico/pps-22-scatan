package scatan.lib.game

import scatan.BaseTest
import scatan.model.map.GameMap

class RulesTest extends BaseTest:

  import EmptyDomain.*
  val players = Seq(Player("Alice"), Player("Bob"), Player("Carol"))
  val emptyGameRules = EmptyDomain.rules

  "The Rules" should "exists" in {
    Rules
  }

  it should "be validatable" in {
    emptyGameRules.valid shouldBe true
  }

  it should "have a initial phase" in {
    emptyGameRules.startingPhase shouldBe a[MyPhases]
    emptyGameRules.startingPhase shouldBe MyPhases.Game
  }

  it should "have a initial steps" in {
    emptyGameRules.startingSteps shouldBe a[Map[MyPhases, Steps]]
    emptyGameRules.startingSteps shouldBe Map(MyPhases.Game -> Steps.Initial, MyPhases.GameOver -> Steps.Initial)
  }

  it should "have a actions" in {
    emptyGameRules.actions shouldBe a[Map[GameStatus[MyPhases, Steps], Map[Actions, Steps]]]
    emptyGameRules.actions shouldBe Map(
      GameStatus(MyPhases.Game, Steps.Initial) -> Map(
        Actions.StartGame -> Steps.Initial,
        Actions.NextTurn -> Steps.ChangingTurn
      ),
      GameStatus(MyPhases.Game, Steps.ChangingTurn) -> Map()
    )
  }

  it should "have a allowed players sizes" in {
    emptyGameRules.allowedPlayersSizes shouldBe a[Set[Int]]
    emptyGameRules.allowedPlayersSizes shouldBe Set(2, 3, 4)
  }

  it should "have a turn iterator factories" in {
    emptyGameRules.phaseTurnIteratorFactories shouldBe a[Map[MyPhases, Seq[Player] => Iterator[Player]]]
    emptyGameRules.phaseTurnIteratorFactories.get(MyPhases.Game) shouldBe a[Some[Seq[Player] => Iterator[Player]]]
    val iterator = emptyGameRules.phaseTurnIteratorFactories(MyPhases.Game)(players)
    iterator shouldBe a[Iterator[Player]]
    iterator.toSeq shouldBe players
  }

  it should "have a next phase" in {
    emptyGameRules.nextPhase shouldBe a[Map[MyPhases, MyPhases]]
    emptyGameRules.nextPhase shouldBe Map(MyPhases.Game -> MyPhases.GameOver, MyPhases.GameOver -> MyPhases.GameOver)
  }

  it should "have a ending steps" in {
    emptyGameRules.endingSteps shouldBe a[Map[MyPhases, Steps]]
    emptyGameRules.endingSteps shouldBe Map(
      MyPhases.Game -> Steps.ChangingTurn,
      MyPhases.GameOver -> Steps.ChangingTurn
    )
  }

  it should "have a winner" in {
    emptyGameRules.winnerFunction shouldBe a[State => Option[Player]]
    emptyGameRules.winnerFunction(State()) shouldBe None
  }

  it should "have allowed actions" in {
    emptyGameRules.allowedActions shouldBe a[Map[GameStatus[MyPhases, Steps], Set[Actions]]]
    emptyGameRules.allowedActions shouldBe Map(
      GameStatus(MyPhases.Game, Steps.Initial) -> Set(Actions.StartGame, Actions.NextTurn),
      GameStatus(MyPhases.Game, Steps.ChangingTurn) -> Set()
    )
  }

  it should "have a next steps" in {
    emptyGameRules.nextSteps shouldBe a[Map[(GameStatus[MyPhases, Steps], Actions), Steps]]
    emptyGameRules.nextSteps shouldBe Map(
      (GameStatus(MyPhases.Game, Steps.Initial), Actions.StartGame) -> Steps.Initial,
      (GameStatus(MyPhases.Game, Steps.Initial), Actions.NextTurn) -> Steps.ChangingTurn
    )
  }

  it should "have an initial action for each phase" in {
    emptyGameRules.initialAction shouldBe a[Map[MyPhases, State => State]]
  }
