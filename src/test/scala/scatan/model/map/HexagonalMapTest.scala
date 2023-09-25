package scatan.model.map

import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scatan.BaseTest

class HexagonalMapTest extends BaseTest with ScalaCheckPropertyChecks:

  val rangeToTest: Range = 0 to 5

  "Hexagonal tiles in GameMap" should "obey to cubic coordinates rules" in {
    rangeToTest foreach { (layer: Int) =>
      val map = HexagonalTiledMap(layer)
      map.tiles.foreach { (hexagon: Hexagon) =>
        hexagon.row + hexagon.col + hexagon.slice shouldBe 0
      }
    }
  }

  it should "be $1 + \\sum_{n=0}^{layer} 6*n$" in {
    rangeToTest foreach { (layer: Int) =>
      val map = HexagonalTiledMap(layer)
      map.tiles.size shouldBe 1 + (0 to layer).map(6 * _).sum
    }
  }

  "Spots in GameMap" should "be $\\sum_{n=0}^{layer} 6 + 12*n$" in {
    rangeToTest foreach { (layer: Int) =>
      val map = HexagonalTiledMap(layer)
      map.nodes.size shouldBe (0 to layer).map(6 + 12 * _).sum
    }
  }

  "Roads in GameMap" should "be $\\sum_{n=0}^{layer} 6 + 18*n$" in {
    rangeToTest foreach { (layer: Int) =>
      val map = HexagonalTiledMap(layer)
      map.edges.size shouldBe (0 to layer).map(6 + 18 * _).sum
    }
  }

  "Every spot in GameMap" should "be over 3 tiles" in {
    rangeToTest foreach { (layer: Int) =>
      val map = HexagonalTiledMap(layer)
      map.nodes.foreach { (spot: Spot) =>
        spot.toSet.size shouldBe 3
      }
    }
  }

  "A Spot in the center" should "have 3 roads" in {
    val map = HexagonalTiledMap(1)
    val centeredHexagon = Hexagon(0, 0, 0)
    for spot <- map.nodes.filter(_ contains centeredHexagon)
    do map.edges.filter(_ contains spot) should have size 3
  }

  "A Spot near border" should "have 2 roads" in {
    val map = HexagonalTiledMap(0)
    for spot <- map.nodes
    do map.edges.filter(_ contains spot) should have size 2
  }
