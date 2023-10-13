package scatan.model.game.state.ops

import scatan.model.components.BuildingType.Road
import scatan.model.components.DevelopmentType.*
import scatan.model.components.{DevelopmentCard, DevelopmentType, ResourceCard, ResourceType}
import scatan.model.game.config.ScatanPlayer
import scatan.model.game.state.ScatanState
import scatan.model.game.state.ops.AwardsOps.awards
import scatan.model.game.state.ops.BuildingOps.build
import scatan.model.game.state.ops.ResourceCardOps.{assignResourceCard, removeResourceCard}
import scatan.model.game.state.ops.RobberOps.moveRobber
import scatan.model.map.{Hexagon, RoadSpot}

object DevelopmentCardOps:

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

    /** Consumes a development card for a given player and returns a new ScatanState with the updated development cards
      * and assigned awards.
      *
      * @param player
      *   the player who is consuming the development card
      * @param developmentCard
      *   the development card to be consumed
      * @return
      *   Some(ScatanState) if the player has the development card, None otherwise
      */
    def removeDevelopmentCard(player: ScatanPlayer, developmentCard: DevelopmentCard): Option[ScatanState] =
      if !state.developmentCards(player).contains(developmentCard) then None
      else
        val remainingCards = state
          .developmentCards(player)
          .foldLeft((Seq.empty[DevelopmentCard], false)) {
            case ((cards, false), card) if card == developmentCard => (cards, true)
            case ((cards, removed), card)                          => (cards :+ card, removed)
          }
          ._1
        Some(
          state.copy(
            developmentCards = state.developmentCards.updated(player, remainingCards),
            assignedAwards = state.awards
          )
        )

    private def playDevelopment(
        player: ScatanPlayer,
        developmentType: DevelopmentType,
        turnNumber: Int
    )(effect: ScatanState => Option[ScatanState]): Option[ScatanState] =
      val stateWithCardConsumed = for
        developmentCards <- state.developmentCards.get(player)
        card <- developmentCards.find(card =>
          card.developmentType == developmentType && !card.played && card.drewAt.isDefined && card.drewAt.get < turnNumber
        )
        stateWithCardConsumed <- state.removeDevelopmentCard(player, card)
        newState <-
          if card.developmentType == Knight then
            stateWithCardConsumed.assignDevelopmentCard(player, card.copy(played = true))
          else Some(stateWithCardConsumed)
      yield newState
      stateWithCardConsumed.flatMap(effect) match
        case None  => Some(state)
        case other => other

    def playKnightDevelopment(player: ScatanPlayer, robberPosition: Hexagon, turnNumber: Int): Option[ScatanState] =
      playDevelopment(player, DevelopmentType.Knight, turnNumber)(_.moveRobber(robberPosition))

    def playRoadBuildingDevelopment(
        player: ScatanPlayer,
        firstRoad: RoadSpot,
        secondRoad: RoadSpot,
        turnNumber: Int
    ): Option[ScatanState] =
      playDevelopment(player, DevelopmentType.RoadBuilding, turnNumber) {
        _.build(firstRoad, Road, player).flatMap(_.build(secondRoad, Road, player))
      }

    def playMonopolyDevelopment(
        player: ScatanPlayer,
        resourceType: ResourceType,
        turnNumber: Int
    ): Option[ScatanState] =
      playDevelopment(player, DevelopmentType.Monopoly, turnNumber) { newState =>
        val otherPlayers = newState.players.filterNot(_ == player)
        otherPlayers.foldLeft(Option(newState))((optState, otherPlayer) =>
          optState.flatMap(state =>
            val resourceCards = state.resourceCards(otherPlayer)
            val resourceCardsToSteal = resourceCards.filter(_.resourceType == resourceType)
            resourceCardsToSteal.foldLeft(Option(state))((optState, resourceCard) =>
              optState.flatMap(state =>
                state
                  .removeResourceCard(otherPlayer, resourceCard)
                  .flatMap(_.assignResourceCard(player, resourceCard))
              )
            )
          )
        )
      }

    def playYearOfPlentyDevelopment(
        player: ScatanPlayer,
        firstResource: ResourceType,
        secondResource: ResourceType,
        turnNumber: Int
    ): Option[ScatanState] =
      playDevelopment(player, DevelopmentType.YearOfPlenty, turnNumber) {
        _.assignResourceCard(player, ResourceCard(firstResource))
          .flatMap(_.assignResourceCard(player, ResourceCard(secondResource)))
      }
