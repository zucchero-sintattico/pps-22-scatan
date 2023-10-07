package scatan.model.game.ops

import scatan.model.game.ScatanState
import scatan.model.game.config.ScatanPlayer
import scatan.model.components.ResourceCard
import scatan.model.game.ops.CardOps.removeResourceCard
import scatan.model.game.ops.CardOps.assignResourceCard

object TradeOps:
  extension (state: ScatanState)
    def tradeWithPlayer(
        sender: ScatanPlayer,
        receiver: ScatanPlayer,
        senderCards: Seq[ResourceCard],
        receiverCards: Seq[ResourceCard]
    ): Option[ScatanState] =
      val stateWithSenderCardsRemoved = senderCards.foldLeft(Option(state)) { (state, card) =>
        state.flatMap(_.removeResourceCard(sender, card))
      }
      val stateWithReceiverCardsRemoved = receiverCards.foldLeft(stateWithSenderCardsRemoved) { (state, card) =>
        state.flatMap(_.removeResourceCard(receiver, card))
      }
      val stateWithSenderCardsAdded = receiverCards.foldLeft(stateWithReceiverCardsRemoved) { (state, card) =>
        state.flatMap(_.assignResourceCard(sender, card))
      }
      val stateWithReceiverCardsAdded = senderCards.foldLeft(stateWithSenderCardsAdded) { (state, card) =>
        state.flatMap(_.assignResourceCard(receiver, card))
      }
      stateWithReceiverCardsAdded
