package scatan.model.map

import scala.annotation.targetName
import Listable.*
import Resources.*
import UnproductiveTerrain.*

object Listable:
  extension (count: Int)
    /** Small DSL to create a list of something.
      *
      * @param count
      * @return
      */
    @targetName("listOf")
    final def *[T](elem: T): Seq[T] = List.fill(count)(elem)

/** Resources of a tile.
  */
enum Resources:
  case WOOD, SHEEP, GRAIN, ROCK, BRICK

/** Unproductive terrain.
  */
enum UnproductiveTerrain:
  case DESERT

type Terrain = Resources | UnproductiveTerrain

/** A mixin that add the concept of Terrains to a Map.
  */
trait MapWithTerrain extends HexTiledMap:

  /** Get the terrain under the tile.
    */
  def toTerrain: Map[Hexagon, Terrain]

/** A factory to create terrains.
  */
object TerrainFactory:

  def fixedForLayer2(tiles: Seq[Hexagon]): Map[Hexagon, Terrain] =
    val terrains: List[Terrain] = List(
      4 * WOOD,
      4 * SHEEP,
      4 * GRAIN,
      3 * ROCK,
      3 * BRICK,
      1 * DESERT
    ).flatten
    Map.from(tiles.zip(terrains))
