package scatan.model.game.ops

import scatan.model.game.ScatanState
import scatan.model.game.config.ScatanPlayer
import scatan.model.components.ResourceCard
import scatan.model.game.ops.CardOps.removeResourceCard
import scatan.model.game.ops.CardOps.assignResourceCard
import scatan.views.game.components.ContextMap.resources

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

    def tradeWithBank(
        player: ScatanPlayer,
        playerCards: Seq[ResourceCard],
        bankCards: ResourceCard
    ): Option[ScatanState] =
      if playerCards.sizeIs == 4 && playerCards.forall(_.resourceType == playerCards.head.resourceType) then
        playerCards
          .foldLeft(Option(state))((state, card) => state.flatMap(_.removeResourceCard(player, card)))
          .flatMap(_.assignResourceCard(player, bankCards))
      else None
