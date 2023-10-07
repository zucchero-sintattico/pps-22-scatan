package scatan.model.game

import scatan.BaseTest
import scatan.model.GameMap
import scatan.model.components.{
  AssignmentInfo,
  Award,
  DevelopmentCard,
  DevelopmentCardsOfPlayers,
  ResourceCard,
  ResourceCards
}
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.ops.EmptySpotsOps.emptySpots
import scatan.model.map.{Hexagon, Spot}

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

  it should "have a gameMap" in {
    val state = ScatanState(threePlayers)
    state.gameMap should be(GameMap())
  }

  it should "have assigned buildings" in {
    val state = ScatanState(threePlayers)
    state.assignedBuildings should be(Map.empty[Spot, AssignmentInfo])
  }

  it should "have assigned awards" in {
    val state = ScatanState(threePlayers)
    state.assignedAwards should be(Award.empty())
  }

  it should "have resource cards" in {
    val state = ScatanState(threePlayers)
    state.resourceCards should be(ResourceCards.empty(threePlayers))
  }

  it should "have development cards" in {
    val state = ScatanState(threePlayers)
    state.developmentCards should be(DevelopmentCardsOfPlayers.empty(threePlayers))
  }

  it should "have the robber placement" in {
    val state = ScatanState(threePlayers)
    state.robberPlacement should be(Hexagon(0, 0, 0))
  }
