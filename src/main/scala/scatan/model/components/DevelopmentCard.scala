package scatan.model.components

import scatan.model.game.config.ScatanPlayer

/** Type of possible development cards.
  */
enum DevelopmentType:
  case Knight
  case RoadBuilding
  case YearOfPlenty
  case Monopoly
  case VictoryPoint

/** A development card.
  */
final case class DevelopmentCard(
    developmentType: DevelopmentType,
    drewAt: Option[Int] = None,
    played: Boolean = false
)

/** The development cards hold by the players.
  */
type DevelopmentCards = Map[ScatanPlayer, Seq[DevelopmentCard]]

object DevelopmentCards:
  def empty(players: Seq[ScatanPlayer]): DevelopmentCards =
    players.map(player => (player, Seq.empty[DevelopmentCard])).toMap
