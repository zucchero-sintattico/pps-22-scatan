package scatan.model.game

import scatan.BaseTest
import scatan.model.game.ScatanState
import scatan.model.map.Spot
import scatan.model.map.RoadSpot
import scatan.model.map.StructureSpot
import scatan.model.game.ops.EmptySpotsOps.emptySpots
import scatan.model.game.config.ScatanPlayer

abstract class BasicStateTest extends BaseTest:

  private def players(n: Int): Seq[ScatanPlayer] =
    (1 to n).map(i => ScatanPlayer(s"Player $i"))

  protected def emptySpot(state: ScatanState): Spot = state.emptySpots.head

  val threePlayers = players(3)
  val fourPlayers = players(4)

  "A Scatan State" should "exists" in {
    val state = ScatanState(threePlayers)
  }

  it should "have players" in {
    val state = ScatanState(threePlayers)
    state.players should be(threePlayers)
  }

  it should "not allow fewer than 3 players" in {
    for n <- 0 to 2
    yield assertThrows[IllegalArgumentException] {
      ScatanState(players(n))
    }
  }

  it should "not allow more than 4 players" in {
    for n <- 5 to 10
    yield assertThrows[IllegalArgumentException] {
      ScatanState(players(n))
    }
  }
