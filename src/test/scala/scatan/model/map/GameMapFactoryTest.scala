package scatan.model.map

import scatan.BaseTest

class GameMapFactoryTest extends BaseTest:

  "A GameMapFactory" should "generate a default map" in {
    val map = GameMapFactory.defaultMap
    map should be(GameMap())
  }

  // If the random generate the same map, it will fail. Which is the probability of that? :)
  it should "generate a random map" in {
    val random = GameMapFactory.randomMap
    random should not be GameMap()
  }

  it should "generate a map with a permutation" in {
    val permutations = (1 to 100).map(_ => GameMapFactory.nextPermutation)
    permutations.toSet.size should be > 1
  }
