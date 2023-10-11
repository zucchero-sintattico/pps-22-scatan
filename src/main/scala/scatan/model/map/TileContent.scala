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

trait TileContentConfig:
  def numbers: Seq[Int]
  def terrains: Seq[Terrain]

/** A factory to create terrains.
  */
object TileContentFactory:

  object ConfigForLayer2 extends TileContentConfig:
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

  private def fromConfig(using config: TileContentConfig)(tiles: Seq[Hexagon]): Map[Hexagon, TileContent] =
    val tileContents = config.terrains.zip(config.numbers).map(p => TileContent(p._1, Some(p._2)))
    Map
      .from(tiles.zip(TileContent(Desert, None) +: tileContents))
      .withDefaultValue(TileContent(Sea, None))

  def fixedForLayer2(tiles: Seq[Hexagon]): Map[Hexagon, TileContent] =
    import ConfigForLayer2.*
    given TileContentConfig = ConfigForLayer2
    fromConfig(tiles)

  def randomForLayer2(tiles: Seq[Hexagon]): Map[Hexagon, TileContent] =
    import ConfigForLayer2.*
    import scala.util.Random.shuffle

    given TileContentConfig with
      val terrains = shuffle(ConfigForLayer2.terrains)
      val numbers = shuffle(ConfigForLayer2.numbers)
    fromConfig(tiles)
