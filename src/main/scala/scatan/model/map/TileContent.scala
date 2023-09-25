package scatan.model.map

import scatan.model.map.Listable.*
import scatan.model.map.Resources.*
import scatan.model.map.UnproductiveTerrain.*

import scala.annotation.targetName

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
  case DESERT, SEA

type Terrain = Resources | UnproductiveTerrain

final case class TileContent(terrain: Terrain, number: Option[Int])

/** A mixin that add the concept of Terrains to a Map.
  */
trait MapWithTileContent:

  /** Get the terrain under the tile.
    */
  def toContent: Map[Hexagon, TileContent]

/** A factory to create terrains.
  */
object TileContentFactory:

  def fixedForLayer2(tiles: Seq[Hexagon]): Map[Hexagon, TileContent] =
    val terrains: List[Terrain] = List(
      4 * WOOD,
      4 * SHEEP,
      4 * GRAIN,
      3 * ROCK,
      3 * BRICK
    ).flatten

    val numbers =
      2 :: 12 :: (for
        i <- (3 to 11).toList
        if i != 7
      yield List(i, i)).flatten

    val tileContents =
      terrains.zip(numbers).map(p => TileContent(p._1, Some(p._2)))

    Map
      .from(tiles.zip(TileContent(DESERT, None) :: tileContents))
      .withDefaultValue(TileContent(SEA, None))
