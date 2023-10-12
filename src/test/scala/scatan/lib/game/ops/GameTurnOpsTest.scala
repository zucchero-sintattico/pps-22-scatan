package scatan.lib.game.ops

import scatan.BaseTest
import scatan.lib.game.EmptyDomain.Actions.NextTurn
import scatan.lib.game.EmptyDomain.{EmptyDomainRules, MyPhases, Player}
import scatan.lib.game.ops.GamePlayOps.play
import scatan.lib.game.ops.GameTurnOps.nextTurn
import scatan.lib.game.ops.RulesOps.{withNextPhase, withStartingStep}
import scatan.lib.game.{EmptyDomain, Game, GameStatus}
import scatan.model.GameMap

class GameTurnOpsTest extends BaseTest:

  import EmptyDomain.*
  given EmptyDomainRules = EmptyDomain.rules
  val players = Seq(Player("p1"), Player("p2"), Player("p3"))

  "A Game" should "allow to change turn" in {
    val game = Game(GameMap(), players)
    val newGame = game.play(NextTurn)(using NextTurnEffect)
    newGame should be(defined)
    newGame.get.turn.player should be(players(1))
  }

  it should "also change the phase if the iterator is empty" in {
    val game = Game(GameMap(), players)
    var newGame = game
    newGame.gameStatus should be(GameStatus(MyPhases.Game, Steps.Initial))
    newGame = newGame.play(NextTurn)(using NextTurnEffect).get
    newGame.gameStatus should be(GameStatus(MyPhases.Game, Steps.Initial))
    newGame = newGame.play(NextTurn)(using NextTurnEffect).get
    newGame.gameStatus should be(GameStatus(MyPhases.Game, Steps.Initial))
    newGame = newGame.play(NextTurn)(using NextTurnEffect).get
    newGame.gameStatus should be(GameStatus(MyPhases.GameOver, Steps.Initial))
  }
