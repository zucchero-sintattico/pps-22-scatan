package scatan.model.development

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import scatan.BaseTest
import scatan.model.scatangame.DevelopmentType

class DevelopmentTypeTest extends BaseTest:

  "A DevelopmentType" should "exists" in {
    val developmentType: DevelopmentType = DevelopmentType.Knight
  }

  it should "be able to be a knight" in {
    val developmentType: DevelopmentType = DevelopmentType.Knight
  }

  it should "be able to be a victory point" in {
    val developmentType: DevelopmentType = DevelopmentType.VictoryPoint
  }

  it should "be able to be a road building" in {
    val developmentType: DevelopmentType = DevelopmentType.RoadBuilding
  }

  it should "be able to be a monopoly" in {
    val developmentType: DevelopmentType = DevelopmentType.Monopoly
  }

  it should "be able to be a year of plenty" in {
    val developmentType: DevelopmentType = DevelopmentType.YearOfPlenty
  }
