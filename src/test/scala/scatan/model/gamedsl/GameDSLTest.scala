package scatan.model.gamedsl

import scatan.model.game.{Action, Game, GameRuleDSL}
import scatan.{BaseTest, model}

class GameDSLTest extends BaseTest:

  case class MyState()
  enum MyPhases:
    case Setup, Play

  enum MyActions(effect: MyState => MyState = identity) extends Action(effect):
    case DoSomething

  type MyGame = Game[MyState, MyPhases, MyActions]
  type MyGameDSL = GameRuleDSL[MyState, MyPhases, MyActions]

  "The GameRules DSL" should "retrieve a configuration" in {
    object GameRules extends MyGameDSL
    GameRules.configuration shouldNot be(null)
  }

  it should "allow to specify the starting phase" in {
    object GameRules extends MyGameDSL:
      import MyPhases.*
      Start withPhase Setup
    GameRules.configuration.initialPhase shouldBe Some(MyPhases.Setup)
  }

  it should "allow to specify the initial state" in {
    object GameRules extends MyGameDSL:
      Start withState MyState()
      GameRules.configuration.initialState shouldBe Some(MyState())
  }

  it should "allow to specify the number of players" in {
    object GameRules extends MyGameDSL:
      Players canBe (3 to 4)
    GameRules.configuration.playersSizes shouldBe (3 to 4)

    object GameWithSpecifiedNumberOfPlayers extends MyGameDSL:
      Players canBe 3
    GameWithSpecifiedNumberOfPlayers.configuration.playersSizes shouldBe Seq(3)
  }

  it should "allow to specify the phase in which a turn can end" in {
    object GameRules extends MyGameDSL:
      import MyPhases.*
      Turn canEndIn Play
    GameRules.configuration.endingPhase shouldBe Some(MyPhases.Play)
  }

  it should "allow to specify the phase map" in {
    object GameRules extends MyGameDSL:
      import MyActions.*
      import MyPhases.*
      When in Setup phase { case DoSomething =>
        Play
      }
    GameRules.configuration.phasesMap(MyPhases.Setup)(MyActions.DoSomething) shouldBe MyPhases.Play
  }
