package scatan.model.resource

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import scatan.BaseTest
import scatan.model.game.{ResourceType, ResourceCard}

class ResourceCardTest extends BaseTest:

  "A ResourceCard" should "exists" in {
    val resourceCard: ResourceCard = ResourceCard(ResourceType.Brick)
  }

  it should "have a resource type" in {
    val resourceCard: ResourceCard = ResourceCard(ResourceType.Brick)
    resourceCard.resourceType should be(ResourceType.Brick)
  }
