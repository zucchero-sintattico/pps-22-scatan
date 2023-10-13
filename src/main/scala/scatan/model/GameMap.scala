package scatan.model

import scatan.model.map.*
import scatan.model.map.HexagonInMap.*
import scatan.model.components.Terrain

/** Hexagonal tiled game map of Scatan.
  *
  * @param withTerrainLayers
  *   number of concentric circles of hexagons of terrains
  *
  * @param withSeaLayers
  *   number of concentric circles of hexagons the terrain ones.
  */
final case class GameMap(
    withTerrainLayers: Int = 2,
    withSeaLayers: Int = 1,
    tileContentsStrategy: TileContentStrategy = TileContentStrategyFactory.fixedForLayer2
) extends HexagonalTiledMap(withTerrainLayers + withSeaLayers)
    with MapWithTileContent:

  val totalLayers = withTerrainLayers + withSeaLayers
  val tileWithTerrain = tiles.toSeq.filter(_.layer <= withTerrainLayers)

  override val toContent: Map[Hexagon, TileContent] = tileContentsStrategy(tileWithTerrain)

  override def equals(x: Any): Boolean =
    x match
      case that: GameMap =>
        this.withTerrainLayers == that.withTerrainLayers &&
        this.withSeaLayers == that.withSeaLayers &&
        (this.toContent.toSet & that.toContent.toSet).sizeIs == this.toContent.size
      case _ => false

object GameMapFactory:

  def defaultMap: GameMap =
    GameMap(tileContentsStrategy = TileContentStrategyFactory.fixedForLayer2)

  def randomMap: GameMap =
    GameMap(tileContentsStrategy = TileContentStrategyFactory.randomForLayer2)

  val strategies: Iterator[TileContentStrategy] = TileContentStrategyFactory.permutationForLayer2.toIterator
  def nextPermutation: GameMap =
    GameMap(tileContentsStrategy = strategies.next())
