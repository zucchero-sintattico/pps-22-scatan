package scatan.model

import scatan.mvc.lib.Model

enum Color:
  case Red
  case Blue
  case Green
  case Yellow

final case class Player(
    name: String,
    color: Color,
    buildings: Seq[Building] = Seq.empty,
    resources: Seq[ResourceCard] = Seq.empty,
    developmentCards: Seq[DevelopmentCard] = Seq.empty
)

extension (player: Player)
  def addBuilding(building: Building): Player =
    player.copy(buildings = player.buildings :+ building)

  def addResource(resource: ResourceCard): Player =
    player.copy(resources = player.resources :+ resource)

  def addDevelopmentCard(developmentCard: DevelopmentCard): Player =
    player.copy(developmentCards = player.developmentCards :+ developmentCard)
