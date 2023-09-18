package scatan.model.map

import scala.annotation.targetName
import scala.annotation.tailrec

/** Terrain of a tile.
  */
enum Terrain:
  case WOOD, SHEEP, GRAIN, ROCK, BRICK, DESERT

  /** Small DSL to create a list of content.
    *
    * @param count
    * @return
    */
  @targetName("listOf")
  final def *:(count: Int): Seq[Terrain] = 0 until count map (_ => this)

type Tile = Hexagon

/** A mixin that add the concept of Terrains to a Map.
  */
trait MapWithTerrain extends HexTiledMap:

  /** Get the terrain under the tile.
    */
  def toTerrain: PartialFunction[Tile, Terrain]

/** A factory to create terrains.
  */
object TerrainFactory:

  import Terrain.*
  def fixedForLayer2(tiles: Seq[Tile]): Map[Tile, Terrain] =
    val terrains = List(
      4 *: WOOD,
      4 *: SHEEP,
      4 *: GRAIN,
      3 *: ROCK,
      3 *: BRICK,
      1 *: DESERT
    ).flatten
    Map.from(tiles.zip(terrains))
