package scatan.model

import scatan.BaseTest
import scatan.model.map.Hexagon
import scatan.utils.UnorderedTriple
import game.{Player, Turn, Game}

class GameActionsTest extends BaseTest:

  val players = Seq(Player("a"), Player("b"), Player("c"), Player("d"))

  "A Game" should "allow to get possible actions" in {
    val game = Game(players)
    game.allowedActions shouldBe Set(ActionsType.Roll)
  }

  it should "allow to check if an action is allowed" in {
    val game = Game(players)
    game.isAllowed(ActionsType.Roll) shouldBe true
  }

  it should "allow to play an action" in {
    val game = Game(players)
    val gameAfterRoll = game.play(Actions.Roll(2))
    gameAfterRoll.currentTurn shouldBe Turn(1, players(0), Phases.Playing)
  }

  it should "not allow to play an action if the game is over" in {
    val endedGame = Game(
      players = Seq(Player("a"), Player("b"), Player("c"), Player("d")),
      currentTurn = Turn(1, Player("a")),
      isOver = true
    )
    assertThrows[IllegalStateException] {
      endedGame.play(Actions.Roll(2))
    }
  }

  it should "not allow to play an action if the action is not allowed" in {
    val game = Game(players)
    assertThrows[IllegalArgumentException] {
      game.play(Actions.BuyDevelopmentCard)
    }
  }

  it should "allow to play a sequence of actions" in {
    val game = Game(players)
    val gameAfterRoll = game.play(Actions.Roll(2))
    val gameAfterNextTurn = gameAfterRoll.play(Actions.NextTurn)
    gameAfterNextTurn.currentTurn shouldBe Turn(2, players(1))
  }

  it should "work with a circular turn" in {
    val game = Game(players)
    def nextTurn(game: Game): Game =
      game.play(Actions.Roll(2)).play(Actions.NextTurn)

    val playersIterator = Iterator.continually(players).flatten
    val gamePlayerIterator = Iterator.iterate(game)(nextTurn).map(game => game.currentPlayer)
    playersIterator.take(100).toList shouldBe gamePlayerIterator.take(100).toList
  }

  it should "have a current phase" in {
    val game = Game(players)
    game.currentPhase shouldBe Phases.Initial
  }

  it should "react to actions" in {
    val game = Game(players)
    val gameAfterRoll = game.play(Actions.Roll(2))
    gameAfterRoll.currentPhase shouldBe Phases.Playing
    val gameAfterSeven = game.play(Actions.Roll(7))
    gameAfterSeven.currentPhase shouldBe Phases.PlaceRobber
  }
