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

  it should "have a drewAt value" in {
    val developmentCard: DevelopmentCard = DevelopmentCard(DevelopmentType.Knight)
    developmentCard.drewAt should be(None)
    val developmentCard2: DevelopmentCard = DevelopmentCard(DevelopmentType.Knight, Some(1))
    developmentCard2.drewAt should be(Some(1))
  }
