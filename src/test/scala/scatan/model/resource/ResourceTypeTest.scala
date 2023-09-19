package scatan.model.resource

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import scatan.model.ResourceType
import scatan.BaseTest

class ResourceTypeTest extends BaseTest:

  "A ResourceType" should "exists" in {
    val resourceType: ResourceType = ResourceType.Brick
    resourceType should not be null
  }

  it should "be able to be a wood" in {
    val resourceType: ResourceType = ResourceType.Wood
    resourceType should be(ResourceType.Wood)
  }

  it should "be able to be a brick" in {
    val resourceType: ResourceType = ResourceType.Brick
    resourceType should be(ResourceType.Brick)
  }

  it should "be able to be a sheep" in {
    val resourceType: ResourceType = ResourceType.Sheep
    resourceType should be(ResourceType.Sheep)
  }

  it should "be able to be a wheat" in {
    val resourceType: ResourceType = ResourceType.Wheat
    resourceType should be(ResourceType.Wheat)
  }

  it should "be able to be a rock" in {
    val resourceType: ResourceType = ResourceType.Rock
    resourceType should be(ResourceType.Rock)
  }
