package scatan.model.game

import scatan.BaseTest
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.ops.EmptySpotsOps.emptySpots
import scatan.model.map.Spot

abstract class BaseScatanStateTest extends BaseTest:

  protected def players(n: Int): Seq[ScatanPlayer] =
    (1 to n).map(i => ScatanPlayer(s"Player $i"))

  protected def emptySpot(state: ScatanState): Spot = state.emptySpots.head

  val threePlayers = players(3)
  val fourPlayers = players(4)
