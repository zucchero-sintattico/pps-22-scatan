package scatan.model.map

import Listable.*

/** A mixin that add the concept of Numbers over a Terrain.
  */
trait MapWithNumbers extends MapWithTerrain:

  /** Possible that could be over the tiles.
    */
  def possibleNumbers: Seq[Int]

  /** Map of numbers over the tiles.
    */
  def toNumber: Map[Hexagon, Int] =
    tiles
      .filterNot(toTerrain(_) == UnproductiveTerrain.DESERT)
      .zip(possibleNumbers)
      .toMap

/** A factory to create numbers sequence for map.
  */
object NumbersFactory:

  def fixedForLayer2(tiles: Seq[Hexagon]): Seq[Int] =
    2 :: 12 :: (for
      i <- (3 to 11).toList
      if i != 7
    yield List(i, i)).flatten
