package scatan.model.game.state

import scatan.model.game.ScatanState
import scatan.lib.game.Player
import scatan.model.components.ResourceCard
import scatan.model.components.DevelopmentCard

object CardsManagement:

  extension (state: ScatanState)
    /** Assigns a resource card to a player and returns a new ScatanState with the updated resourceCards map.
      *
      * @param player
      *   the player to assign the resource card to
      * @param resourceCard
      *   the resource card to assign to the player
      * @return
      *   a new ScatanState with the updated resourceCards map
      */
    def assignResourceCard(player: Player, resourceCard: ResourceCard): ScatanState =
      state.copy(
        resourceCards = state.resourceCards.updated(player, state.resourceCards(player) :+ resourceCard)
      )

    /** Returns a new ScatanState with the given development card assigned to the given player. The development card is
      * added to the player's list of development cards. The assigned awards remain the same.
      *
      * @param player
      *   The player to assign the development card to.
      * @param developmentCard
      *   The development card to assign to the player.
      * @return
      *   A new ScatanState with the development card assigned to the player.
      */
    def assignDevelopmentCard(player: Player, developmentCard: DevelopmentCard): ScatanState =
      state.copy(
        developmentCards = state.developmentCards.updated(player, state.developmentCards(player) :+ developmentCard),
        assignedAwards = state.awards
      )

    /** Consumes a development card for a given player and returns a new ScatanState with the updated development cards
      * and assigned awards.
      * @param player
      *   the player who is consuming the development card
      * @param developmentCard
      *   the development card to be consumed
      * @return
      *   a new ScatanState with the updated development cards and assigned awards
      */
    def consumeDevelopmentCard(player: Player, developmentCard: DevelopmentCard): ScatanState =
      val remainingCards =
        state.developmentCards(player).filter(_.developmentType == developmentCard.developmentType).drop(1)
      state.copy(
        developmentCards = state.developmentCards.updated(player, remainingCards),
        assignedAwards = state.awards
      )
