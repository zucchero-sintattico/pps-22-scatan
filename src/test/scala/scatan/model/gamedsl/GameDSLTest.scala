package scatan.model.gamedsl

import scatan.model.game.{Action, Game, GameRuleDSL}
import scatan.{BaseTest, model}

class GameDSLTest extends BaseTest:

  enum MyPhases:
    case Setup, Play

  enum MyActions(effect: Game[?, ?] => Game[?, ?] = identity) extends Action(effect):
    case DoSomething

  "The GameRules DSL" should "retrieve a configuration" in {
    object GameRules extends GameRuleDSL[?, ?]
    GameRules.configuration shouldNot be(null)
  }

  it should "allow to specify the starting phase" in {
    object GameRules extends GameRuleDSL[MyPhases, ?]:
      import MyPhases.*
      Start withPhase Setup
    GameRules.configuration.initialPhase shouldBe Some(MyPhases.Setup)
  }

  it should "allow to specify the number of players" in {
    object GameRules extends GameRuleDSL[?, ?]:
      Players canBe (3 to 4)
    GameRules.configuration.playersSizes shouldBe (3 to 4)

    object GameWithSpecifiedNumberOfPlayers extends GameRuleDSL[?, ?]:
      Players canBe 3
    GameWithSpecifiedNumberOfPlayers.configuration.playersSizes shouldBe Seq(3)
  }

  it should "allow to specify the phase in which a turn can end" in {
    object GameRules extends GameRuleDSL[MyPhases, ?]:
      import MyPhases.*
      Turn canEndIn Play
    GameRules.configuration.endingPhase shouldBe Some(MyPhases.Play)
  }

  it should "allow to specify the phase map" in {
    object GameRules extends GameRuleDSL[MyPhases, MyActions]:
      import MyActions.*
      import MyPhases.*
      When in Setup phase { case DoSomething =>
        Play
      }
    GameRules.configuration.phasesMap(MyPhases.Setup)(MyActions.DoSomething) shouldBe MyPhases.Play
  }
