package scatan.model.map

import scatan.BaseTest
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scatan.model.GameMap
import scatan.utils.UnorderedTriple
import scatan.utils.UnorderedPair

class GameMapWithGraphOpsTest extends BaseTest with ScalaCheckPropertyChecks:

  val standardGameMap = GameMap(2)

  private object BoilerplateForTest:
    val spotToTest = UnorderedTriple(Hexagon(0, 0, 0), Hexagon(-1, 0, 1), Hexagon(-1, 1, 0))
    val edgesOfTestedSpot = Set(
      UnorderedPair(
        UnorderedTriple(Hexagon(0, 0, 0), Hexagon(-1, 0, 1), Hexagon(-1, 1, 0)),
        UnorderedTriple(Hexagon(-1, 1, 0), Hexagon(-1, 0, 1), Hexagon(-2, 1, 1))
      ),
      UnorderedPair(
        UnorderedTriple(Hexagon(0, 0, 0), Hexagon(0, 1, -1), Hexagon(-1, 1, 0)),
        UnorderedTriple(Hexagon(0, 0, 0), Hexagon(-1, 0, 1), Hexagon(-1, 1, 0))
      ),
      UnorderedPair(
        UnorderedTriple(Hexagon(0, 0, 0), Hexagon(-1, 0, 1), Hexagon(-1, 1, 0)),
        UnorderedTriple(Hexagon(0, 0, 0), Hexagon(-1, 0, 1), Hexagon(0, -1, 1))
      )
    )
    val neighboursOfTestedSpot = Set(
      UnorderedTriple(Hexagon(0, 0, 0), Hexagon(-1, 0, 1), Hexagon(0, -1, 1)),
      UnorderedTriple(Hexagon(0, 0, 0), Hexagon(0, 1, -1), Hexagon(-1, 1, 0)),
      UnorderedTriple(Hexagon(-1, 1, 0), Hexagon(-1, 0, 1), Hexagon(-2, 1, 1))
    )

  import BoilerplateForTest.*

  "The tested Spot" should "have 3 edges" in {
    standardGameMap.edgesOf(spotToTest).size shouldBe 3
  }

  it should "have 3 neighbours" in {
    standardGameMap.neighboursOf(spotToTest).size shouldBe 3
  }

  "The edges of the tested Spot" should "are as expected" in {
    standardGameMap.edgesOf(spotToTest) shouldBe edgesOfTestedSpot
  }

  "The neighbours of the tested Spot" should "are as expected" in {
    standardGameMap.neighboursOf(spotToTest) shouldBe neighboursOfTestedSpot
  }

  they should "are neighbours of the tested Spot" in {
    standardGameMap.neighboursOf(spotToTest).foreach { neighbourOfTestedSpot =>
      standardGameMap.neighboursOf(neighbourOfTestedSpot) should contain(spotToTest)
    }
  }
