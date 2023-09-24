package scatan.model.resource

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import scatan.BaseTest
import scatan.model.scatangame.ResourceType

class ResourceTypeTest extends BaseTest:

  "A ResourceType" should "exists" in {
    val resourceType: ResourceType = ResourceType.Wood
  }

  it should "be able to be a wood" in {
    val resourceType: ResourceType = ResourceType.Wood
  }

  it should "be able to be a brick" in {
    val resourceType: ResourceType = ResourceType.Brick
  }

  it should "be able to be a sheep" in {
    val resourceType: ResourceType = ResourceType.Sheep
  }

  it should "be able to be a wheat" in {
    val resourceType: ResourceType = ResourceType.Wheat
  }

  it should "be able to be a rock" in {
    val resourceType: ResourceType = ResourceType.Rock
  }
