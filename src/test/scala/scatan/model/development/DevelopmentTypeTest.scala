package scatan.model.development

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import scatan.model.DevelopmentType

class DevelopmentTypeTest extends AnyFlatSpec with should.Matchers:

  "A DevelopmentType" should "exists" in {
    val developmentType: DevelopmentType = DevelopmentType.Knight
    developmentType should not be null
  }

  it should "be able to be a knight" in {
    val developmentType: DevelopmentType = DevelopmentType.Knight
    developmentType should be(DevelopmentType.Knight)
  }

  it should "be able to be a victory point" in {
    val developmentType: DevelopmentType = DevelopmentType.VictoryPoint
    developmentType should be(DevelopmentType.VictoryPoint)
  }

  it should "be able to be a road building" in {
    val developmentType: DevelopmentType = DevelopmentType.RoadBuilding
    developmentType should be(DevelopmentType.RoadBuilding)
  }

  it should "be able to be a monopoly" in {
    val developmentType: DevelopmentType = DevelopmentType.Monopoly
    developmentType should be(DevelopmentType.Monopoly)
  }

  it should "be able to be a year of plenty" in {
    val developmentType: DevelopmentType = DevelopmentType.YearOfPlenty
    developmentType should be(DevelopmentType.YearOfPlenty)
  }
