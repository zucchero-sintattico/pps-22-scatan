package scatan.model.map

import scatan.BaseTest
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class GameMapTest extends BaseTest with ScalaCheckPropertyChecks:

  "Hexagonal tiles in GameMap" should "obey to cubic coordinates rules" in {
    0 to 10 foreach { (layer: Int) =>
      val gameMap = GameMap(layer)
      gameMap.tiles.foreach { (hexagon: Hexagon) =>
        hexagon.row + hexagon.col + hexagon.slice shouldBe 0
      }
    }
  }

  it should "be $1 + \\sum_{n=0}^{layer} 6*n$" in {
    0 to 10 foreach { (layer: Int) =>
      val gameMap = GameMap(layer)
      gameMap.tiles.size shouldBe 1 + (0 to layer).map(6 * _).sum
    }
  }
