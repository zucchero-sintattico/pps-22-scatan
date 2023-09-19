package scatan.model

import scatan.mvc.lib.Model

enum ResourceType:
  case Wood
  case Brick
  case Sheep
  case Wheat
  case Rock

final case class ResourceCard(resourceType: ResourceType)
