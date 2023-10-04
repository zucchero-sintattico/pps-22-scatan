package scatan.lib.game

import scatan.BaseTest

class RulesTest extends BaseTest:

  case class State()
  case class Player(name: String)
  enum Phases:
    case Game
  enum Steps:
    case Initial
  enum Actions:
    case StartGame

  val emptyGameRules = Rules[State, Phases, Steps, Actions, Player](
    initialStateFactory = (_) => State(),
    initialPhase = Phases.Game,
    initialSteps = Map(Phases.Game -> Steps.Initial),
    actions = Map(GameStatus(Phases.Game, Steps.Initial) -> Map(Actions.StartGame -> Steps.Initial)),
    allowedPlayersSizes = Set(2, 3, 4),
    turnIteratorFactories = Map(Phases.Game -> (_ => players.iterator)),
    nextPhase = Map(Phases.Game -> Phases.Game),
    endingSteps = Map(Phases.Game -> Steps.Initial),
    winner = (_ => None)
  )

  val players = Seq(Player("Alice"), Player("Bob"), Player("Carol"))

  "The Rules" should "exists" in {
    Rules
  }

  it should "have a empty ruleset" in {
    Rules.empty
  }

  it should "have an initial state factory" in {
    emptyGameRules.initialStateFactory shouldBe a[Seq[Player] => State]
    emptyGameRules.initialStateFactory(players) shouldBe a[State]
    emptyGameRules.initialStateFactory(players) shouldBe State()
  }

  it should "have a initial phase" in {
    emptyGameRules.initialPhase shouldBe a[Phases]
    emptyGameRules.initialPhase shouldBe Phases.Game
  }

  it should "have a initial steps" in {
    emptyGameRules.initialSteps shouldBe a[Map[Phases, Steps]]
    emptyGameRules.initialSteps shouldBe Map(Phases.Game -> Steps.Initial)
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
    emptyGameRules.turnIteratorFactories shouldBe a[Map[Phases, Seq[Player] => Iterator[Player]]]
    emptyGameRules.turnIteratorFactories.get(Phases.Game) shouldBe a[Some[Seq[Player] => Iterator[Player]]]
    val iterator = emptyGameRules.turnIteratorFactories(Phases.Game)(players)
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
    emptyGameRules.winner shouldBe a[State => Option[Player]]
    emptyGameRules.winner(State()) shouldBe None
  }

  it should "have allowed actions" in {
    emptyGameRules.allowedActions shouldBe a[Map[GameStatus[Phases, Steps], Set[Actions]]]
    emptyGameRules.allowedActions shouldBe Map(
      GameStatus(Phases.Game, Steps.Initial) -> Set(Actions.StartGame)
    )
  }

  it should "have a next steps" in {
    emptyGameRules.nextSteps shouldBe a[Map[GameStatus[Phases, Steps], Steps]]
    emptyGameRules.nextSteps shouldBe Map(
      GameStatus(Phases.Game, Steps.Initial) -> Steps.Initial
    )
  }
  
  
