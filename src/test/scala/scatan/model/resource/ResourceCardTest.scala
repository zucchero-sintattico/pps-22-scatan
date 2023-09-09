package scatan.model.resource

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import scatan.model.{ResourceCard, ResourceType}

class ResourceCardTest extends AnyFlatSpec with should.Matchers:

  "A ResourceCard" should "exists" in {
    val resourceCard: ResourceCard = ResourceCard(ResourceType.Brick)
    resourceCard should not be null
  }

  it should "have a resource type" in {
    val resourceCard: ResourceCard = ResourceCard(ResourceType.Brick)
    resourceCard.resourceType should be(ResourceType.Brick)
  }
