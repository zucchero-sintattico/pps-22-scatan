package scatan.model.components

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import scatan.BaseTest
import scatan.model.components.{DevelopmentCard, DevelopmentType}

class DevelopmentCardTest extends BaseTest:

  "A DevelopmentCard" should "exists" in {
    val developmentCard: DevelopmentCard = DevelopmentCard(DevelopmentType.Knight)
  }

  it should "have a development type" in {
    val developmentCard: DevelopmentCard = DevelopmentCard(DevelopmentType.Knight)
    developmentCard.developmentType should be(DevelopmentType.Knight)
  }
