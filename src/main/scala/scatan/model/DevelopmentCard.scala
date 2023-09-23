package scatan.model

import scatan.lib.mvc.Model

enum DevelopmentType:
  case Knight
  case RoadBuilding
  case YearOfPlenty
  case Monopoly
  case VictoryPoint

final case class DevelopmentCard(developmentType: DevelopmentType)
type DevelopmentCardsOfPlayers = Map[Player, Seq[DevelopmentCard]]

object DevelopmentCardsOfPlayers:
  def empty(players: Seq[Player]): DevelopmentCardsOfPlayers =
    players.map(player => (player, Seq.empty[DevelopmentCard])).toMap
