package scatan.model.game.ops

import scatan.model.game.ScatanState
import scatan.model.game.config.ScatanPlayer
import scatan.model.components.ResourceCard
import scatan.model.game.ops.CardOps.removeResourceCard
import scatan.model.game.ops.CardOps.assignResourceCard
import scatan.views.game.components.ContextMap.resources

object TradeOps:
  val tradeWithBankRequiredCards = 4
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

    /** Trade with the bank The player must have 4 cards of the same type The bank will give 1 card of the same type
      *
      * @param player,
      *   the player that will trade with the bank
      * @param playerCards,
      *   the cards that the player will give to the bank
      * @param bankCard,
      *   the card that the bank will give to the player
      * @return
      *   Some(state) if the trade is allowed, None otherwise
      */
    def tradeWithBank(
        player: ScatanPlayer,
        playerCards: Seq[ResourceCard],
        bankCard: ResourceCard
    ): Option[ScatanState] =
      if playerCards.sizeIs == tradeWithBankRequiredCards && playerCards.forall(
          _.resourceType == playerCards.head.resourceType
        )
      then
        playerCards
          .foldLeft(Option(state))((state, card) => state.flatMap(_.removeResourceCard(player, card)))
          .flatMap(_.assignResourceCard(player, bankCard))
      else None
