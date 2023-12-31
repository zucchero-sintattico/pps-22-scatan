package scatan.model.game.state.ops

import scatan.model.components.{ResourceCard, ResourceType}
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.state.ScatanState
import scatan.model.game.state.ops.ResourceCardOps.{assignResourceCard, removeResourceCard}

/** Operations on [[ScatanState]] related to trades.
  */
object TradeOps:
  val tradeWithBankRequiredCards = 4
  extension (state: ScatanState)

    /** Trade between two players. The sender must have the senderCards and the receiver must have the receiverCards The
      * sender will give the senderCards to the receiver and vice versa
      *
      * @param sender
      *   the player that will trade with the receiver
      * @param receiver
      *   the player that will trade with the sender
      * @param senderCards
      *   the cards that the sender will give to the receiver
      * @param receiverCards
      *   the cards that the receiver will give to the sender
      * @return
      *   Some(state) if the trade is allowed, None otherwise
      */
    def tradeBetweenPlayers(
        sender: ScatanPlayer,
        receiver: ScatanPlayer,
        senderCards: Seq[ResourceCard],
        receiverCards: Seq[ResourceCard]
    ): Option[ScatanState] =
      val stateWithSenderCardsProcessed = senderCards.foldLeft(Option(state))((s, card) =>
        for
          initialState <- s
          stateWithCardRemovedFromSender <- initialState.removeResourceCard(sender, card)
          stateWithCardAssignedToReceiver <- stateWithCardRemovedFromSender.assignResourceCard(receiver, card)
        yield stateWithCardAssignedToReceiver
      )
      val stateWithReceiverCardsProcessed = receiverCards.foldLeft(stateWithSenderCardsProcessed)((s, card) =>
        for
          initialState <- s
          stateWithCardRemovedFromReceiver <- initialState.removeResourceCard(receiver, card)
          stateWithCardAssignedToSender <- stateWithCardRemovedFromReceiver.assignResourceCard(sender, card)
        yield stateWithCardAssignedToSender
      )
      stateWithReceiverCardsProcessed

    /** Trade with the bank The player must have 4 cards of the same type The bank will give 1 card of the same type
      *
      * @param player,
      *   the player that will trade with the bank
      * @param playerCardsType,
      *   the cards type that the player will give to the bank
      * @param bankCardType,
      *   the card type that the bank will give to the player
      * @return
      *   Some(state) if the trade is allowed, None otherwise
      */
    def tradeWithBank(
        player: ScatanPlayer,
        playerCardsType: ResourceType,
        bankCardType: ResourceType
    ): Option[ScatanState] =
      if state.resourceCards(player).count(_.resourceType == playerCardsType) >= tradeWithBankRequiredCards
      then
        val stateWithPlayerCardsRemoved = (1 to tradeWithBankRequiredCards).foldLeft(Option(state))((state, _) =>
          state.flatMap(_.removeResourceCard(player, ResourceCard(playerCardsType)))
        )
        stateWithPlayerCardsRemoved.flatMap(_.assignResourceCard(player, ResourceCard(bankCardType)))
      else None
