package scatan.model.scatangame

import scatan.lib.mvc.Model
import scatan.model.game.Player

enum ResourceType:
  case Wood
  case Brick
  case Sheep
  case Wheat
  case Rock

final case class ResourceCard(resourceType: ResourceType)

type ResourceCards = Map[Player, Seq[ResourceCard]]

object ResourceCard:
  def empty(players: Seq[Player]): ResourceCards =
    players.map(player => (player, Seq.empty[ResourceCard])).toMap
