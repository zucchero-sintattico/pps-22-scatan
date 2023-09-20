package scatan.model

import scatan.BaseTest

class GameActionsTest extends BaseTest:

  val players = Seq(Player("a"), Player("b"), Player("c"), Player("d"))

  "A Game" should "allow to get possible actions" in {
    val game = Game(players)
    game.possibleActions shouldBe Set(Action.Roll, Action.RollSeven)
  }

  it should "allow to check if an action is allowed" in {
    val game = Game(players)
    game.isAllowed(Action.Roll) shouldBe true
  }

  it should "allow to play an action" in {
    val game = Game(players)
    val gameAfterRoll = game.play(Action.Roll)
    gameAfterRoll.currentTurn shouldBe Turn(1, players(0), Phase.Playing)
  }

  it should "not allow to play an action if the game is over" in {
    val endedGame = Game(
      players = Seq(Player("a"), Player("b"), Player("c"), Player("d")),
      currentTurn = Turn(1, Player("a")),
      isOver = true
    )
    assertThrows[IllegalStateException] {
      endedGame.play(Action.Roll)
    }
  }

  it should "not allow to play an action if the action is not allowed" in {
    val game = Game(players)
    assertThrows[IllegalArgumentException] {
      game.play(Action.Build)
    }
  }

  it should "allow to play a sequence of actions" in {
    val game = Game(players)
    val gameAfterRoll = game.play(Action.Roll)
    val gameAfterBuild = gameAfterRoll.play(Action.Build)
    val gameAfterNextTurn = gameAfterBuild.play(Action.NextTurn)
    gameAfterNextTurn.currentTurn shouldBe Turn(2, players(1))
  }

  it should "work with a circular turn" in {
    val game = Game(players)
    def nextTurn(game: Game): Game =
      game.play(Action.Roll).play(Action.NextTurn)
    val playersIterator = Iterator.continually(players).flatten
    val gamePlayerIterator = Iterator.iterate(game)(nextTurn).map(_.currentPlayer)
    playersIterator.take(100).toList shouldBe gamePlayerIterator.take(100).toList
  }

  it should "have a current phase" in {
    val game = Game(players)
    game.currentPhase shouldBe Phase.Initial
  }

  it should "react to actions" in {
    val game = Game(players)
    val gameAfterRoll = game.play(Action.Roll)
    gameAfterRoll.currentPhase shouldBe Phase.Playing
  }
