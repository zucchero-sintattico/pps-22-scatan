package scatan.model.map

import scatan.model.components.Listable.`*`
import scatan.model.components.ResourceType.*
import scatan.model.components.Terrain
import scatan.model.components.UnproductiveTerrain.*

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
      4 * Wood,
      4 * Sheep,
      4 * Wheat,
      3 * Rock,
      3 * Brick
    ).flatten

    val numbers =
      2 :: 12 :: (for
        i <- (3 to 11).toList
        if i != 7
      yield List(i, i)).flatten

    val tileContents =
      terrains.zip(numbers).map(p => TileContent(p._1, Some(p._2)))

    Map
      .from(tiles.zip(TileContent(Desert, None) :: tileContents))
      .withDefaultValue(TileContent(Sea, None))
