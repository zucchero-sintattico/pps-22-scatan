package scatan.model.map

import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scatan.BaseTest
import scatan.model.{GameMap, Spot}
import scatan.utils.{UnorderedPair, UnorderedTriple}

class GameMapTest extends BaseTest with ScalaCheckPropertyChecks:

  val rangeToTest: Range = 0 to 4
  val standardGameMap = GameMap(2)

  "Hexagonal tiles in GameMap" should "obey to cubic coordinates rules" in {
    rangeToTest foreach { (layer: Int) =>
      val gameMap = GameMap(layer)
      gameMap.tiles.foreach { (hexagon: Hexagon) =>
        hexagon.row + hexagon.col + hexagon.slice shouldBe 0
      }
    }
  }

  it should "be $1 + \\sum_{n=0}^{layer} 6*n$" in {
    rangeToTest foreach { (layer: Int) =>
      val gameMap = GameMap(layer)
      gameMap.tiles.size shouldBe 1 + (0 to layer).map(6 * _).sum
    }
  }

  "Spots in GameMap" should "be $\\sum_{n=0}^{layer} 6 + 12*n$" in {
    rangeToTest foreach { (layer: Int) =>
      val gameMap = GameMap(layer)
      gameMap.nodes.size shouldBe (0 to layer).map(6 + 12 * _).sum
    }
  }

  "Roads in GameMap" should "be $\\sum_{n=0}^{layer} 6 + 18*n$" in {
    rangeToTest foreach { (layer: Int) =>
      val gameMap = GameMap(layer)
      gameMap.edges.size shouldBe (0 to layer).map(6 + 18 * _).sum
    }
  }

  "Every spot in GameMap" should "be over 3 tiles" in {
    rangeToTest foreach { (layer: Int) =>
      val gameMap = GameMap(layer)
      gameMap.nodes.foreach { (spot: Spot) =>
        spot.toSet.size shouldBe 3
      }
    }
  }

  "A Spot in the center" should "have 3 roads" in {
    val gameMap = GameMap(1)
    val centeredHexagon = Hexagon(0, 0, 0)
    for spot <- gameMap.nodes.filter(_ contains centeredHexagon)
    do gameMap.edges.filter(_ contains spot) should have size 3
  }

  "A Spot near border" should "have 2 roads" in {
    val gameMap = GameMap(0)
    for spot <- gameMap.nodes
    do gameMap.edges.filter(_ contains spot) should have size 2
  }

  "A standard GameMap" should "have Terrains" in {
    standardGameMap.tiles.foreach { (hexagon: Hexagon) =>
      standardGameMap.toTerrain.isDefinedAt(hexagon) shouldBe true
    }
  }
