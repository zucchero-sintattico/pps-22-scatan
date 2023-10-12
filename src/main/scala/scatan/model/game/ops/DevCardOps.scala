package scatan.model.game.ops

import scatan.model.components.{DevelopmentCard, ResourceType}
import scatan.model.game.ScatanState
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.ops.AwardOps.awards

object DevCardOps:

  extension (state: ScatanState)
    /** Returns a new ScatanState with the given development card assigned to the given player. The development card is
      * added to the player's list of development cards. The assigned awards are updated.
      *
      * @param player
      *   The player to assign the development card to.
      * @param developmentCard
      *   The development card to assign to the player.
      * @return
      *   Some(ScatanState) if the player added the development card, None otherwise
      */
    def assignDevelopmentCard(player: ScatanPlayer, developmentCard: DevelopmentCard): Option[ScatanState] =
      Some(
        state.copy(
          developmentCards = state.developmentCards.updated(player, state.developmentCards(player) :+ developmentCard),
          assignedAwards = state.awards
        )
      )

    /** Buys a development card for a given player and returns a new ScatanState with the updated resource cards and
      * development cards. The player must have the required resources to buy the development card.
      *
      * @param player,
      *   the player who is buying the development card
      * @param turnNumber,
      *   the turn number when the development card was bought
      * @return
      *   Some(ScatanState) if the player bought the development card, None otherwise
      */
    def buyDevelopmentCard(player: ScatanPlayer, turnNumber: Int): Option[ScatanState] =
      val requiredResources = Seq(
        ResourceType.Wheat,
        ResourceType.Sheep,
        ResourceType.Rock
      )
      val playerResources = state.resourceCards(player)
      val hasRequiredResources = requiredResources.forall(playerResources.map(_.resourceType).contains)
      if !hasRequiredResources then None
      else
        val card = state.developmentCardsDeck.headOption
        val cardWithTurnNumber = card.map(_.copy(drewAt = Some(turnNumber)))
        cardWithTurnNumber match
          case Some(developmentCard) =>
            val updatedResources = requiredResources.foldLeft(playerResources)((resources, resource) =>
              resources.filterNot(_.resourceType == resource)
            )
            Some(
              state.copy(
                resourceCards = state.resourceCards.updated(player, updatedResources),
                developmentCards =
                  state.developmentCards.updated(player, state.developmentCards(player) :+ developmentCard),
                developmentCardsDeck = state.developmentCardsDeck.tail
              )
            )
          case None => None

    /** Consumes a development card for a given player and returns a new ScatanState with the updated development cards
      * and assigned awards.
      * @param player
      *   the player who is consuming the development card
      * @param developmentCard
      *   the development card to be consumed
      * @return
      *   Some(ScatanState) if the player has the development card, None otherwise
      */
    def consumeDevelopmentCard(player: ScatanPlayer, developmentCard: DevelopmentCard): Option[ScatanState] =
      if !state.developmentCards(player).contains(developmentCard) then None
      else
        val remainingCards =
          state.developmentCards(player).filter(_.developmentType == developmentCard.developmentType).drop(1)
        Some(
          state.copy(
            developmentCards = state.developmentCards.updated(player, remainingCards),
            assignedAwards = state.awards
          )
        )
