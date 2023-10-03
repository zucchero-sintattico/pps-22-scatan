package scatan.model

import scatan.BaseTest
import scatan.lib.game.dsl.GameDSL
import scatan.lib.game.ops.Effect
import scatan.lib.game.ops.GamePlayOps.play
import scatan.lib.game.ops.GameWinOps.{isOver, winner}
import scatan.lib.game.{Game, GameRulesDSL, GameStatus}
import scatan.model.game.ScatanActions.RollDice
import scatan.model.game.{ScatanActions, ScatanDSL, ScatanPhases, ScatanPlayer, ScatanState, ScatanSteps}

class GameTest extends BaseTest:

  type ScatanGame = Game[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer]
  type ScatanDSL = GameDSL[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer]

  given ScatanRules = ScatanDSL.rules

  private def players(n: Int): Seq[ScatanPlayer] =
    (1 to n).map(i => ScatanPlayer(s"Player $i"))

  val threePlayers: Seq[ScatanPlayer] = players(3)
  val fourPlayers: Seq[ScatanPlayer] = players(4)

  "A Game" should "exists" in {
    val game: ScatanGame = null
  }

  it should "have players" in {
    val game = Game(threePlayers)
    game.players should be(threePlayers)
  }

  it should "expose if the game is over" in {
    val game = Game(threePlayers)
    game.isOver shouldBe false
  }

  it should "have a winner when the game is over" in {
    val game = Game(threePlayers)
    game.winner shouldBe None
  }

  it should "take players" in {
    val game = Game(threePlayers)
    game.players should be(threePlayers)
  }

  it should "not allow fewer than 3 players" in {
    for n <- 0 to 2
    yield assertThrows[IllegalArgumentException] {
      Game(players(n))
    }
  }

  it should "not allow more than 4 players" in {
    for n <- 5 to 10
    yield assertThrows[IllegalArgumentException] {
      Game(players(n))
    }
  }

  it should "have a status" in {
    val game = Game(threePlayers)
    game.status shouldBe GameStatus(ScatanPhases.Setup, ScatanSteps.SetupSettlement)
  }

  it should "have a turn" in {
    val game = Game(threePlayers)
    game.turn.number shouldBe 1
    game.turn.player shouldBe threePlayers.head
  }

  def nextTurn(game: ScatanGame): ScatanGame =
    given effect: Effect[RollDice.type, ScatanState] with
      def apply(state: ScatanState): ScatanState =
        identity(state)
    game.play(RollDice).get

  it should "allow to change turn" in {
    val game = Game(threePlayers)

    val newGame = nextTurn(game)
    println(newGame)
    newGame.turn.number shouldBe 2
    newGame.turn.player shouldBe threePlayers(1)
  }

  it should "do a circular turn" in {
    var game = Game(threePlayers)
    def nextTurn(game: ScatanGame): ScatanGame =
      game.play(RollDice(1)).nextTurn
    for i <- 1 to 10
    do
      game.turn.number shouldBe i
      game.turn.player shouldBe threePlayers(((i - 1) % 3))
      game = nextTurn(game)
  }
