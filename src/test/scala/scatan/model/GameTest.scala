package scatan.model

import scatan.BaseTest
import scatan.lib.game.{Game, GameRulesDSL, Player}
import scatan.model.game.ScatanActions.{BuildRoad, RollDice}
import scatan.model.game.{ScatanActions, ScatanPhases, ScatanRules, ScatanState, ScatanStateImpl}
import scatan.model.map.{RoadSpot, StructureSpot}

class GameTest extends BaseTest:

  type ScatanGame = Game[ScatanState, ScatanPhases, ScatanActions]
  type ScatanGameDSL = GameRulesDSL[ScatanState, ScatanPhases, ScatanActions]

  given ScatanGameDSL = ScatanRules

  private def players(n: Int): Seq[Player] =
    (1 to n).map(i => Player(s"Player $i"))

  val threePlayers: Seq[Player] = players(3)
  val fourPlayers: Seq[Player] = players(4)

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

  it should "be endable" in {
    val config = ScatanRules.configuration
    config.initialState = Some(ScatanState.ended)
    val game = Game(threePlayers)(config)
    game.isOver shouldBe true
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

  it should "have a phase" in {
    val game = Game(threePlayers)
    game.phase shouldBe ScatanPhases.InitialSettlmentAssignment
  }

  it should "have a turn" in {
    val game = Game(threePlayers)
    game.turn.number shouldBe 1
    game.turn.player shouldBe threePlayers(0)
  }

  it should "allow to change turn" in {
    val game = Game(threePlayers)
    def nextTurn(game: ScatanGame): ScatanGame =
      val firstStructureSpot = game.state.emptySpot.find(_.isInstanceOf[StructureSpot]).get.asInstanceOf[StructureSpot]
      val firstRoadSpot = game.state.emptySpot.find(_.isInstanceOf[RoadSpot]).get.asInstanceOf[RoadSpot]
      game
        .play(ScatanActions.BuildSettlement(firstStructureSpot, game.turn.player))
        .play(ScatanActions.BuildRoad(firstRoadSpot, game.turn.player))
        .nextTurn
    val newGame = nextTurn(game)
    println(newGame)
    newGame.turn.number shouldBe 2
    newGame.turn.player shouldBe threePlayers(1)
  }

  it should "do a circular turn" in {
    var game = Game(threePlayers)
    def nextTurn(game: ScatanGame): ScatanGame =
      val firstStructureSpot = game.state.emptySpot.find(_.isInstanceOf[StructureSpot]).get.asInstanceOf[StructureSpot]
      val firstRoadSpot = game.state.emptySpot.find(_.isInstanceOf[RoadSpot]).get.asInstanceOf[RoadSpot]
      game
        .play(ScatanActions.BuildSettlement(firstStructureSpot, game.turn.player))
        .play(ScatanActions.BuildRoad(firstRoadSpot, game.turn.player))
        .nextTurn
    for i <- 1 to 10
    do
      game.turn.number shouldBe i
      game.turn.player shouldBe threePlayers(((i - 1) % 3))
      game = nextTurn(game)
  }
