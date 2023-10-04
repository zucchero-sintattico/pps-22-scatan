package scatan.lib.game.ops

import scatan.lib.game.Turn

object TurnOps:

  extension [Player](turn: Turn[Player])
    /** Returns the next turn, with the given player as the current player.
      * @param player
      *   The player who will be the current player in the next turn.
      * @return
      *   The next turn.
      */
    def next(player: Player): Turn[Player] = Turn(turn.number + 1, player)
