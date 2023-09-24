package scatan.model.components

import scatan.lib.game.Player

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
