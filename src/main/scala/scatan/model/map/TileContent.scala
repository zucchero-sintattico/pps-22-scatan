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

/** A configuration to generate the content of the tiles.
  */
trait TileContentConfig:
  def numbers: Seq[Int]
  def terrains: Seq[Terrain]

/** A strategy to generate the content of the tiles.
  */
type TileContentStrategy = Seq[Hexagon] => Map[Hexagon, TileContent]

/** A factory to create strategies to generate the content of the tiles.
  */
object TileContentStrategyFactory:

  /** Configuration for the layer 2 of the map.
    */
  object ConfigForLayer2 extends TileContentConfig:
    val terrains: List[Terrain] = List(
      1 * Desert,
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

  private def fromConfig(using config: TileContentConfig): TileContentStrategy =
    val iterator = config.numbers.iterator
    val tileContents = config.terrains.map { t =>
      t match
        case Desert => TileContent(t, None)
        case _      => TileContent(t, iterator.nextOption())
    }
    tiles =>
      Map
        .from(tiles.zip(tileContents))
        .withDefaultValue(TileContent(Sea, None))

  /** A strategy that generates a fixed content for the layer 2 of the map.
    *
    * @return
    *   the strategy.
    */
  def fixedForLayer2: TileContentStrategy =
    import ConfigForLayer2.*
    given TileContentConfig = ConfigForLayer2
    fromConfig

  /** A strategy that generates a random content for the layer 2 of the map.
    *
    * @return
    *   the strategy.
    */
  def randomForLayer2: TileContentStrategy =
    import ConfigForLayer2.*

    import scala.util.Random.shuffle
    given TileContentConfig with
      val terrains = shuffle(ConfigForLayer2.terrains)
      val numbers = shuffle(ConfigForLayer2.numbers)
    fromConfig

  private def removeAtPos[A](list: List[A], n: Int): List[A] =
    val splitted = list.splitAt(n)
    splitted._1 ::: splitted._2.tail

  private def permutations[A](list: List[A]): LazyList[List[A]] = list match
    case Nil => LazyList(Nil)
    case _ =>
      for
        i <- list.indices.to(LazyList)
        e = list(i)
        r = removeAtPos(list, i)
        pr <- permutations(r)
      yield e :: pr

  /** A strategy that generates all the possible permutations of the content for the layer 2 of the map.
    *
    * @return
    *   the LazyList of strategies.
    */
  def permutationForLayer2: LazyList[TileContentStrategy] =
    import ConfigForLayer2.*
    for
      t <- permutations(terrains)
      n <- permutations(numbers.toList)
    yield
      given TileContentConfig with
        val terrains = t
        val numbers = n
      fromConfig
