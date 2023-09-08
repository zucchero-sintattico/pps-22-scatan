package scatan.model

import scatan.utils.UnorderedTriple
import scatan.utils.UnorderedTriple.given
import scatan.utils.UnorderedPair
import scatan.model.map.HexTiledMap.*
import scatan.model.map.{Hexagon, HexTiledMap, UndirectedGraph}

/** A Spot is unique identified by three hexagons in the map.
  *
  * @param hexagons
  */
type Spot = UnorderedTriple[Hexagon]

/** A Road connect two spots.
  */
type Road = UnorderedPair[Spot]

/** Hexagonal tiled GameMap of Scatan.
  *
  * @param layers
  *   number of concentric circles of hexagons
  */
class GameMap(layers: Int) extends HexTiledMap with UndirectedGraph[Spot, Road]:

  override def tiles: Set[Hexagon] =
    (for
      r <- -layers to layers
      q <- -layers to layers
      s <- -layers to layers
      if r + q + s == 0
    yield Hexagon(r, q, s)).toSet

  override def nodes: Set[Spot] =
    for
      hex <- tiles
      first <- hex.neighbours
      second <- hex.neighbours
      if first.isNeighbour(second)
    yield UnorderedTriple(hex, first, second)

  val spotsPerRoad = 2
  override def edges: Set[Road] =
    for
      first <- nodes
      second <- nodes
      if first.toSet.intersect(second.toSet).sizeIs == spotsPerRoad
    yield UnorderedPair(first, second)
