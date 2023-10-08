package scatan.model.components

import scatan.model.game.config.ScatanPlayer

enum DevelopmentType:
  case Knight
  case RoadBuilding
  case YearOfPlenty
  case Monopoly
  case VictoryPoint

final case class DevelopmentCard(
    developmentType: DevelopmentType,
    drewAt: Option[Int] = None
)

type DevelopmentCards = Map[ScatanPlayer, Seq[DevelopmentCard]]

object DevelopmentCards:
  def empty(players: Seq[ScatanPlayer]): DevelopmentCards =
    players.map(player => (player, Seq.empty[DevelopmentCard])).toMap
