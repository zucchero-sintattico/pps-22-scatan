package scatan.model.components

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import scatan.BaseTest
import scatan.model.components.BuildingType.*
import scatan.model.components.ResourceType.*

class BuildingTypeTest extends BaseTest:

  "A BuildingType" should "exists" in {
    val buildingType: BuildingType = BuildingType.Settlement
  }

  it should "be able to be a settlement" in {
    val buildingType: BuildingType = BuildingType.Settlement
  }

  it should "be able to be a city" in {
    val buildingType: BuildingType = BuildingType.City
  }

  it should "be able to be a road" in {
    val buildingType: BuildingType = BuildingType.Road
  }

class BuildingTypeCostTest extends BaseTest:

  "A Settlement" should "cost 1 wood, 1 brick, 1 sheep" in {
    Settlement.cost should be(
      Cost(
        Wood *** 1,
        Brick *** 1,
        Sheep *** 1
      )
    )
  }

  "A City" should "cost 2 wheat, 3 rock" in {
    City.cost should be(
      Cost(
        Wheat *** 2,
        Rock *** 3
      )
    )
  }

  "A Road" should "cost 1 wood, 1 brick" in {
    Road.cost should be(
      Cost(
        Wood *** 1,
        Brick *** 1
      )
    )
  }
