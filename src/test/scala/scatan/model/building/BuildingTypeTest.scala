package scatan.model.building

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import scatan.model.BuildingType
import scatan.model.BuildingType.*
import scatan.model.ResourceType
import scatan.model.Cost

class BuildingTypeTest extends AnyFlatSpec with should.Matchers:

  "A BuildingType" should "exists" in {
    val buildingType: BuildingType = BuildingType.Settlement
    buildingType should not be null
  }

  it should "be able to be a settlement" in {
    val buildingType: BuildingType = BuildingType.Settlement
    buildingType should be(BuildingType.Settlement)
  }

  it should "be able to be a city" in {
    val buildingType: BuildingType = BuildingType.City
    buildingType should be(BuildingType.City)
  }

  it should "be able to be a road" in {
    val buildingType: BuildingType = BuildingType.Road
    buildingType should be(BuildingType.Road)
  }

  it should "have a cost" in {
    BuildingType.Road.cost should be(Cost(ResourceType.Brick * 1, ResourceType.Wood * 1))
    BuildingType.Settlement.cost should be(
      Cost(
        ResourceType.Wood * 1,
        ResourceType.Brick * 1,
        ResourceType.Wheat * 1,
        ResourceType.Sheep * 1
      )
    )
    BuildingType.City.cost should be(Cost(ResourceType.Wheat * 2, ResourceType.Rock * 3))
  }