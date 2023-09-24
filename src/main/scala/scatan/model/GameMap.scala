package scatan.model

import scatan.model.map.HexagonInMap.*
import scatan.model.map.*
import map.{MapWithTileContent, TileContentFactory, TileContent}

/** Hexagonal tiled game map of Scatan.
  *
  * @param withTerrainLayers
  *   number of concentric circles of hexagons of terrains
  *
  * @param withSeaLayers
  *   number of concentric circles of hexagons the terrain ones.
  */
final case class GameMap(withTerrainLayers: Int = 2, withSeaLayers: Int = 1)
    extends HexagonalTiledMap(withTerrainLayers + withSeaLayers)
    with MapWithTileContent:

  val totalLayers = withTerrainLayers + withSeaLayers
  val tileWithTerrain = tiles.toSeq.filter(_.layer <= withTerrainLayers)

  override def toContent: Map[Hexagon, TileContent] =
    TileContentFactory.fixedForLayer2(tileWithTerrain)
