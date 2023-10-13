package scatan.model.game.state.ops

import scatan.model.components.{ResourceCard, ResourceType}
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.state.ScatanState
import scatan.model.game.state.ops.ResourceCardOps.{assignResourceCard, removeResourceCard}
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
      val stateWithSenderCardsProcessed = senderCards.foldLeft(Option(state))((state, card) =>
        state
          .flatMap(s => s.removeResourceCard(sender, card))
          .flatMap(s => s.assignResourceCard(receiver, card))
      )
      val stateWithReceiverCardsProcessed = receiverCards.foldLeft(stateWithSenderCardsProcessed)((state, card) =>
        state
          .flatMap(s => s.removeResourceCard(receiver, card))
          .flatMap(s => s.assignResourceCard(sender, card))
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
