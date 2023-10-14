package scatan.model.map

import scatan.model.components.Terrain
import scatan.model.map.*
import scatan.model.map.HexagonInMap.*

/** Hexagonal tiled game map of Scatan.
  *
  * @param withTerrainLayers
  *   number of concentric circles of hexagons of terrains
  *
  * @param withSeaLayers
  *   number of concentric circles of hexagons the terrain ones.
  *
  * @param tileContentStrategy
  *   strategy to generate the content of the tiles
  */
final case class GameMap(
    withTerrainLayers: Int = 2,
    withSeaLayers: Int = 1,
    tileContentStrategy: TileContentStrategy = TileContentStrategyFactory.fixedForLayer2
) extends HexagonalTiledMap(withTerrainLayers + withSeaLayers)
    with MapWithTileContent:

  val totalLayers = withTerrainLayers + withSeaLayers
  val tileWithTerrain = tiles.toSeq.filter(_.layer <= withTerrainLayers)

  override val toContent: Map[Hexagon, TileContent] = tileContentStrategy(tileWithTerrain)

  override def equals(x: Any): Boolean =
    x match
      case that: GameMap =>
        this.withTerrainLayers == that.withTerrainLayers &&
        this.withSeaLayers == that.withSeaLayers &&
        (this.toContent.toSet & that.toContent.toSet).sizeIs == this.toContent.size
      case _ => false

/** A factory to create game maps.
  */
object GameMapFactory:

  /** @return
    *   a fixed game map for layer 2
    */
  def defaultMap: GameMap =
    GameMap(tileContentStrategy = TileContentStrategyFactory.fixedForLayer2)

  /** @return
    *   a random game map for layer 2
    */
  def randomMap: GameMap =
    GameMap(tileContentStrategy = TileContentStrategyFactory.randomForLayer2)

  private val strategies: Iterator[TileContentStrategy] = TileContentStrategyFactory.permutationForLayer2.toIterator

  /** @return
    *   the next permutation of the game map for layer 2
    */
  def nextPermutation: GameMap =
    GameMap(tileContentStrategy = strategies.next())
