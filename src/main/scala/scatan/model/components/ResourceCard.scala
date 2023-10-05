package scatan.model.components

import scatan.model.game.config.ScatanPlayer

enum ResourceType:
  case Wood
  case Brick
  case Sheep
  case Wheat
  case Rock

final case class ResourceCard(resourceType: ResourceType)

type ResourceCards = Map[ScatanPlayer, Seq[ResourceCard]]

object ResourceCard:
  def empty(players: Seq[ScatanPlayer]): ResourceCards =
    players.map(player => (player, Seq.empty[ResourceCard])).toMap
