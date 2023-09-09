package scatan.model.building

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import scatan.model.{Building, BuildingType}

class BuildingTest extends AnyFlatSpec with should.Matchers:

  "A Building" should "exists" in {
    val building: Building = Building(BuildingType.Settlement)
    building should not be null
  }

  it should "have a building type" in {
    val building: Building = Building(BuildingType.Settlement)
    building.buildingType should be(BuildingType.Settlement)
  }
