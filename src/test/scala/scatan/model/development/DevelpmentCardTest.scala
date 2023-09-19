package scatan.model.development

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import scatan.model.{DevelopmentCard, DevelopmentType}
import scatan.BaseTest

class DevelpmentCardTest extends BaseTest:

  "A DevelopmentCard" should "exists" in {
    val developmentCard: DevelopmentCard = DevelopmentCard(DevelopmentType.Knight)
    developmentCard should not be null
  }

  it should "have a development type" in {
    val developmentCard: DevelopmentCard = DevelopmentCard(DevelopmentType.Knight)
    developmentCard.developmentType should be(DevelopmentType.Knight)
  }
