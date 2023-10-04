package scatan.lib.game

import scatan.BaseTest

class RulesTest extends BaseTest:

  import EmptyDomain.*
  val players = Seq(Player("Alice"), Player("Bob"), Player("Carol"))
  val emptyGameRules = EmptyDomain.rules

  "The Rules" should "exists" in {
    Rules
  }

  it should "have a empty ruleset" in {
    Rules.empty
  }

  it should "have an initial state factory" in {
    emptyGameRules.startingStateFactory shouldBe a[Seq[Player] => State]
    emptyGameRules.startingStateFactory(players) shouldBe a[State]
    emptyGameRules.startingStateFactory(players) shouldBe State()
  }

  it should "have a initial phase" in {
    emptyGameRules.startingPhase shouldBe a[Phases]
    emptyGameRules.startingPhase shouldBe Phases.Game
  }

  it should "have a initial steps" in {
    emptyGameRules.startingSteps shouldBe a[Map[Phases, Steps]]
    emptyGameRules.startingSteps shouldBe Map(Phases.Game -> Steps.Initial)
  }

  it should "have a actions" in {
    emptyGameRules.actions shouldBe a[Map[GameStatus[Phases, Steps], Map[Actions, Steps]]]
    emptyGameRules.actions shouldBe Map(
      GameStatus(Phases.Game, Steps.Initial) -> Map(Actions.StartGame -> Steps.Initial)
    )
  }

  it should "have a allowed players sizes" in {
    emptyGameRules.allowedPlayersSizes shouldBe a[Set[Int]]
    emptyGameRules.allowedPlayersSizes shouldBe Set(2, 3, 4)
  }

  it should "have a turn iterator factories" in {
    emptyGameRules.phaseTurnIteratorFactories shouldBe a[Map[Phases, Seq[Player] => Iterator[Player]]]
    emptyGameRules.phaseTurnIteratorFactories.get(Phases.Game) shouldBe a[Some[Seq[Player] => Iterator[Player]]]
    val iterator = emptyGameRules.phaseTurnIteratorFactories(Phases.Game)(players)
    iterator shouldBe a[Iterator[Player]]
    iterator.toSeq shouldBe players
  }

  it should "have a next phase" in {
    emptyGameRules.nextPhase shouldBe a[Map[Phases, Phases]]
    emptyGameRules.nextPhase shouldBe Map(Phases.Game -> Phases.Game)
  }

  it should "have a ending steps" in {
    emptyGameRules.endingSteps shouldBe a[Map[Phases, Steps]]
    emptyGameRules.endingSteps shouldBe Map(Phases.Game -> Steps.Initial)
  }

  it should "have a winner" in {
    emptyGameRules.winnerFunction shouldBe a[State => Option[Player]]
    emptyGameRules.winnerFunction(State()) shouldBe None
  }

  it should "have allowed actions" in {
    emptyGameRules.allowedActions shouldBe a[Map[GameStatus[Phases, Steps], Set[Actions]]]
    emptyGameRules.allowedActions shouldBe Map(
      GameStatus(Phases.Game, Steps.Initial) -> Set(Actions.StartGame)
    )
  }

  it should "have a next steps" in {
    emptyGameRules.nextSteps shouldBe a[Map[(GameStatus[Phases, Steps], Actions), Steps]]
    emptyGameRules.nextSteps shouldBe Map(
      (GameStatus(Phases.Game, Steps.Initial), Actions.StartGame) -> Steps.Initial
    )
  }
