package scatan.model

import scatan.BaseTest
import scatan.model.map.Hexagon
import scatan.utils.UnorderedTriple

class GameActionsTest extends BaseTest:

  val players = Seq(Player("a"), Player("b"), Player("c"), Player("d"))

  "A Game" should "allow to get possible actions" in {
    val game = Game(players)
    game.allowedActions shouldBe Set(ActionType.Roll)
  }

  it should "allow to check if an action is allowed" in {
    val game = Game(players)
    game.isAllowed(ActionType.Roll) shouldBe true
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
      game.play(Action.BuyDevelopmentCard)
    }
  }

  it should "allow to play a sequence of actions" in {
    val game = Game(players)
    val gameAfterRoll = game.play(Action.Roll)
    val gameAfterNextTurn = gameAfterRoll.play(Action.NextTurn)
    gameAfterNextTurn.currentTurn shouldBe Turn(2, players(1))
  }

  it should "work with a circular turn" in {
    val game = Game(players)
    def nextTurn(game: Game): Game =
      val gameAfterRoll = game.play(Action.Roll)
      if gameAfterRoll.currentPhase == Phase.Playing then gameAfterRoll.play(Action.NextTurn)
      else
        val gameAfterPlaceRobber =
          gameAfterRoll.play(
            Action.PlaceRobber(UnorderedTriple(Hexagon(0, 0, 0), Hexagon(0, 1, -1), Hexagon(1, 0, -1)))
          )
        gameAfterPlaceRobber.play(Action.StoleCard(gameAfterPlaceRobber.currentPlayer)).play(Action.NextTurn)

    val playersIterator = Iterator.continually(players).flatten
    val gamePlayerIterator = Iterator.iterate(game)(nextTurn).map(game => game.currentPlayer)
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
