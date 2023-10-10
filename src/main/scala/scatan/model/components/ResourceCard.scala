package scatan.model.components

import scatan.model.game.config.ScatanPlayer

enum ResourceType:
  case Wood
  case Brick
  case Sheep
  case Wheat
  case Rock

object ResourceType:
  def withName(name: String): ResourceType =
    name match
      case "Wood"  => Wood
      case "Brick" => Brick
      case "Sheep" => Sheep
      case "Wheat" => Wheat
      case "Rock"  => Rock

final case class ResourceCard(resourceType: ResourceType)

extension (card: ResourceCard) def **(amount: Int): Seq[ResourceCard] = Seq.fill(amount)(card)

type ResourceCards = Map[ScatanPlayer, Seq[ResourceCard]]

object ResourceCards:
  def empty(players: Seq[ScatanPlayer]): ResourceCards =
    players.map(player => (player, Seq.empty[ResourceCard])).toMap
