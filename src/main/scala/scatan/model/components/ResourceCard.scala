package scatan.model.components

import scatan.model.game.config.ScatanPlayer

/** Type of possible resources.
  */
enum ResourceType:
  case Wood
  case Brick
  case Sheep
  case Wheat
  case Rock

/** A resource card.
  */
final case class ResourceCard(resourceType: ResourceType)

extension (card: ResourceCard) def **(amount: Int): Seq[ResourceCard] = Seq.fill(amount)(card)

/** The resource cards hold by the players.
  */
type ResourceCards = Map[ScatanPlayer, Seq[ResourceCard]]

object ResourceCards:
  def empty(players: Seq[ScatanPlayer]): ResourceCards =
    players.map(player => (player, Seq.empty[ResourceCard])).toMap
