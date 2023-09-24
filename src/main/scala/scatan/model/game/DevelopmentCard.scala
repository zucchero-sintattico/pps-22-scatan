package scatan.model.game

import scatan.lib.mvc.Model

enum DevelopmentType:
  case Knight
  case RoadBuilding
  case YearOfPlenty
  case Monopoly
  case VictoryPoint

final case class DevelopmentCard(developmentType: DevelopmentType)
type DevelopmentCards = Map[Player, Seq[DevelopmentCard]]

object DevelopmentCardsOfPlayers:
  def empty(players: Seq[Player]): DevelopmentCards =
    players.map(player => (player, Seq.empty[DevelopmentCard])).toMap
