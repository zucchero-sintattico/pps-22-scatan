package scatan.model

import scatan.BaseTest
import scatan.lib.game.ops.GamePlayOps.play
import scatan.lib.game.ops.GameTurnOps.nextTurn
import scatan.lib.game.ops.GameWinOps.{isOver, winner}
import scatan.lib.game.{Game, GameStatus, Rules}
import scatan.model.game.ScatanEffects.{AssignRoadEffect, AssignSettlementEffect, NextTurnEffect}
import scatan.model.game.config.ScatanActions.*
import scatan.model.game.config.{ScatanActions, ScatanPhases, ScatanPlayer, ScatanSteps}
import scatan.model.game.ops.EmptySpotsOps.{emptyRoadSpot, emptyStructureSpot}
import scatan.model.game.{ScatanDSL, ScatanState}

class GameTest extends BaseTest:

  type ScatanGame = Game[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer]
  type ScatanRules = Rules[ScatanState, ScatanPhases, ScatanSteps, ScatanActions, ScatanPlayer]

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
    game.gameStatus shouldBe GameStatus(ScatanPhases.Setup, ScatanSteps.SetupSettlement)
  }

  it should "have a turn" in {
    val game = Game(threePlayers)
    game.turn.number shouldBe 1
    game.turn.player shouldBe threePlayers.head
  }

  def nextTurn(game: ScatanGame): Option[ScatanGame] =
    for
      structureSpot <- game.state.emptyStructureSpot.headOption
      gameAfterBuildSettlement <- game.play(AssignSettlement)(using
        AssignSettlementEffect(game.turn.player, structureSpot)
      )
      roadSpot <- gameAfterBuildSettlement.state.emptyRoadSpot.headOption
      gameAfterBuildRoad <- gameAfterBuildSettlement.play(AssignRoad)(using
        AssignRoadEffect(gameAfterBuildSettlement.turn.player, roadSpot)
      )
      newGame <- gameAfterBuildRoad.play(NextTurn)(using NextTurnEffect())
    yield newGame

  it should "allow to change turn" in {
    val game = Game(threePlayers)
    for newGame <- nextTurn(game)
    do
      newGame.turn.number shouldBe 2
      newGame.turn.player shouldBe threePlayers(1)
  }
