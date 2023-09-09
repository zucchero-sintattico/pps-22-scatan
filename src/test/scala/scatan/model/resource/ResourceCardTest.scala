package scatan.model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class ResourceCardTest extends AnyFlatSpec with should.Matchers:

  "A ResourceCard" should "exists" in {
    val resourceCard: ResourceCard = ResourceCard(ResourceType.Brick)
    resourceCard should not be null
  }

  it should "have a resource type" in {
    val resourceCard: ResourceCard = ResourceCard(ResourceType.Brick)
    resourceCard.resourceType should be(ResourceType.Brick)
  }
