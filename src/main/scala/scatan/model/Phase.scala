package scatan.model

/** The possible phases of a turn.
  *
  * @param allowedActions
  *   The actions that are allowed in this phase.
  */
enum Phase(val allowedActions: Set[ActionType]):
  case Initial extends Phase(Set(ActionType.Roll))
  case PlaceRobber extends Phase(Set(ActionType.PlaceRobber))
  case StoleCard extends Phase(Set(ActionType.StoleCard))
  case Playing
      extends Phase(
        Set(
          ActionType.Build,
          ActionType.BuyDevelopmentCard,
          ActionType.PlayDevelopmentCard,
          ActionType.Trade,
          ActionType.NextTurn
        )
      )

  /** Check if the given action is allowed in this phase. */
  def isAllowed(action: ActionType): Boolean = allowedActions.contains(action)
