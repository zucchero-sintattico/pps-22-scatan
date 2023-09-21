package scatan.model.map

import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scatan.BaseTest
import scatan.model.GameMap

class GameMapTest extends BaseTest with ScalaCheckPropertyChecks:

  val standardGameMap = GameMap(2)

  "A standard GameMap" should "have Content in tile" in {
    standardGameMap.tileWithTerrain.foreach { (hexagon: Hexagon) =>
      standardGameMap.toContent.isDefinedAt(hexagon) shouldBe true
    }
  }

  it should "have 19 Terrains" in {
    standardGameMap.tileWithTerrain should have size 19
  }

  it should "have one Desert tile" in {
    val desertTiles =
      standardGameMap.tileWithTerrain
        .filter(standardGameMap.toContent(_).terrain == UnproductiveTerrain.DESERT)
    desertTiles should have size 1
  }

  it should "do not have number over Desert tile" in {
    standardGameMap.tileWithTerrain
      .filter(standardGameMap.toContent(_).terrain == UnproductiveTerrain.DESERT)
      .map(standardGameMap.toContent)
      .map(_.diceResult) should contain only None
  }
