package scatan.model.map

class GameMap(layers: Int) extends HexTiledMap:

  override def tiles: Set[Hexagon] =
    (for
      r <- -layers to layers
      q <- -layers to layers
      s <- -layers to layers
      if r + q + s == 0
    yield Hexagon(r, q, s)).toSet
