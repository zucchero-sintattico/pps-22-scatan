package scatan.lib.game.ops

import scatan.BaseTest
import scatan.lib.game.{EmptyDomain, GameStatus}
import scatan.lib.game.ops.RulesOps.*

class RulesOpsTest extends BaseTest:

  import EmptyDomain.*

  "The Rules" should "allow to specify the players sizes as a Set" in {
    val newRules = rules.withAllowedPlayersSizes(Set(2, 3, 4))
    newRules.allowedPlayersSizes should be(Set(2, 3, 4))
  }

  it should "allow to specify the starting state factory" in {
    val newRules = rules.withStartingStateFactory((_) => State())
    val players = Seq(Player("a"), Player("b"))
    newRules.startingStateFactory(players) should be(EmptyDomain.State())
  }

  it should "allow to specify the starting phase" in {
    val newRules = rules.withStartingPhase(Phases.Game)
    newRules.startingPhase should be(Phases.Game)
  }

  it should "allow to specify the starting step for a given phase" in {
    val newRules = rules.withStartingStep(Phases.Game, Steps.Initial)
    newRules.startingSteps(Phases.Game) should be(Steps.Initial)
  }

  it should "allow to specify the ending step for a given phase" in {
    val newRules = rules.withEndingStep(Phases.Game, Steps.Initial)
    newRules.endingSteps(Phases.Game) should be(Steps.Initial)
  }

  it should "allow to specify the turn iterator factory for a given phase" in {
    val newRules = rules.withPhaseTurnIteratorFactory(Phases.Game, (_.iterator))
    val players = Seq(Player("a"), Player("b"))
    newRules.phaseTurnIteratorFactories(Phases.Game)(players).toSeq should be(players)
  }

  it should "allow to specify the next phase for a given phase" in {
    val newRules = rules.withNextPhase(Phases.Game, Phases.Game)
    newRules.nextPhase(Phases.Game) should be(Phases.Game)
  }

  it should "allow to specify the possible actions with the next step for a given GameStatus" in {
    val newRules = rules.withActions(GameStatus(Phases.Game, Steps.Initial) -> Map(Actions.StartGame -> Steps.Initial))
    newRules.actions(GameStatus(Phases.Game, Steps.Initial)) should be(Map(Actions.StartGame -> Steps.Initial))
  }

  it should "allow to specify a winner function" in {
    val newRules = rules.withWinnerFunction((_) => None)
    newRules.winnerFunction(State()) should be(None)
  }
