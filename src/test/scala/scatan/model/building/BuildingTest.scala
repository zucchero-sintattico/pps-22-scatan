package scatan.model.building

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import scatan.model.{Building, BuildingType}
import scatan.BaseTest

class BuildingTest extends BaseTest:

  "A Building" should "exists" in {
    val building: Building = Building(BuildingType.Settlement)
  }

  it should "have a building type" in {
    val building: Building = Building(BuildingType.Settlement)
    building.buildingType should be(BuildingType.Settlement)
  }
