package scatan.model.map

import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scatan.BaseTest
import scatan.utils.{UnorderedPair, UnorderedTriple}

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
    val edgeToTest = UnorderedPair(
      UnorderedTriple(Hexagon(0, 0, 0), Hexagon(-1, 0, 1), Hexagon(-1, 1, 0)),
      UnorderedTriple(Hexagon(-1, 1, 0), Hexagon(-1, 0, 1), Hexagon(-2, 1, 1))
    )
    val neighbourEdgesOfedgeToTest = Set(
      UnorderedPair(
        UnorderedTriple(Hexagon(0, 0, 0), Hexagon(0, 1, -1), Hexagon(-1, 1, 0)),
        UnorderedTriple(Hexagon(0, 0, 0), Hexagon(-1, 0, 1), Hexagon(-1, 1, 0))
      ),
      UnorderedPair(
        UnorderedTriple(Hexagon(0, 0, 0), Hexagon(-1, 0, 1), Hexagon(-1, 1, 0)),
        UnorderedTriple(Hexagon(0, 0, 0), Hexagon(-1, 0, 1), Hexagon(0, -1, 1))
      ),
      UnorderedPair(
        UnorderedTriple(Hexagon(-1, 1, 0), Hexagon(-1, 0, 1), Hexagon(-2, 1, 1)),
        UnorderedTriple(Hexagon(-2, 0, 2), Hexagon(-1, 0, 1), Hexagon(-2, 1, 1))
      ),
      UnorderedPair(
        UnorderedTriple(Hexagon(-1, 1, 0), Hexagon(-2, 1, 1), Hexagon(-2, 2, 0)),
        UnorderedTriple(Hexagon(-1, 1, 0), Hexagon(-1, 0, 1), Hexagon(-2, 1, 1))
      )
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

  "The tested edge" should "have 4 neighbour edges" in {
    standardGameMap.edgesOfNodesConnectedBy(edgeToTest).size shouldBe 4
  }

  they should "are as expected" in {
    standardGameMap.edgesOfNodesConnectedBy(edgeToTest) shouldBe neighbourEdgesOfedgeToTest
  }
