package scatan.model
import game.Phase

/** The possible phases of a turn.
  *
  * @param allowedActions
  *   The actions that are allowed in this phase.
  */
enum Phases(allowedActions: Set[ActionsType]) extends Phase(allowedActions):
  case Initial extends Phases(Set(ActionsType.Roll))
  case PlaceRobber extends Phases(Set(ActionsType.PlaceRobber))
  case StoleCard extends Phases(Set(ActionsType.StoleCard))
  case Playing
      extends Phases(
        Set(
          ActionsType.Build,
          ActionsType.BuyDevelopmentCard,
          ActionsType.PlayDevelopmentCard,
          ActionsType.Trade,
          ActionsType.NextTurn
        )
      )
