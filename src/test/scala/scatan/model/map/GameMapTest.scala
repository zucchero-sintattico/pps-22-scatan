package scatan.model.map

import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scatan.BaseTest
import scatan.model.GameMap
import scatan.model.components.UnproductiveTerrain.*
import scatan.model.map.HexagonInMap.layer

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
        .filter(standardGameMap.toContent(_).terrain == Desert)
    desertTiles should have size 1
  }

  it should "do not have number over Desert tile" in {
    standardGameMap.tileWithTerrain
      .filter(standardGameMap.toContent(_).terrain == Desert)
      .map(standardGameMap.toContent)
      .map(_.number) should contain only None
  }

  it should "have sea in outer layer" in {
    standardGameMap.tiles
      .filter(_.layer > standardGameMap.withTerrainLayers)
      .map(standardGameMap.toContent)
      .map(_.terrain) should contain only Sea
  }
