package scatan.model.components

import scatan.model.game.ScatanPlayer

enum DevelopmentType:
  case Knight
  case RoadBuilding
  case YearOfPlenty
  case Monopoly
  case VictoryPoint

final case class DevelopmentCard(developmentType: DevelopmentType)
type DevelopmentCards = Map[ScatanPlayer, Seq[DevelopmentCard]]

object DevelopmentCardsOfPlayers:
  def empty(players: Seq[ScatanPlayer]): DevelopmentCards =
    players.map(player => (player, Seq.empty[DevelopmentCard])).toMap
