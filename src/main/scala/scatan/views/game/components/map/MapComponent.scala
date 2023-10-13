package scatan.views.game.components.map

import com.raquo.laminar.api.L.*
import scatan.model.GameMap
import scatan.model.components.UnproductiveTerrain
import scatan.model.map.{Hexagon, TileContent}
import scatan.views.game.components.ContextMap
import scatan.views.game.components.ContextMap.toImgId
import scatan.views.utils.Coordinates
import scatan.views.utils.Coordinates.center

object MapComponent:

  private given hexSize: Int = 100
  private val radius = hexSize / 4
  private val svgCornersPoints: String =
    (for
      i <- 0 to 5
      angleDeg = 60 * i + 30
      angleRad = Math.PI / 180 * angleDeg
      x = hexSize * math.cos(angleRad)
      y = hexSize * math.sin(angleRad)
    yield s"$x,$y").mkString(" ")
  private val layersToCanvasSize: Int => Int = x => (2 * x * hexSize) + 50

  def map: GameMap ?=> Element =
    val gameMap = summon[GameMap]
    val canvasSize = layersToCanvasSize(gameMap.totalLayers)
    svg.svg(
      svgImages,
      svg.viewBox := s"-${canvasSize} -${canvasSize} ${2 * canvasSize} ${2 * canvasSize}",
      for
        hex <- gameMap.tiles.toList
        content = gameMap.toContent(hex)
      yield svgHexagon(hex, content)
    )

  private def svgHexagon(hex: Hexagon, content: TileContent): Element =
    val Coordinates(x, y) = hex.center
    svg.g(
      svg.transform := s"translate($x, $y)",
      svg.polygon(
        svg.points := svgCornersPoints,
        svg.cls := "hexagon",
        svg.fill := s"url(#${content.terrain.toImgId})"
      ),
      content.terrain match
        case UnproductiveTerrain.Sea => ""
        case _                       => circularNumber(hex, content)
    )

  def circularNumber(hex: Hexagon, content: TileContent): Element =
    svg.g(
      svg.circle(
        svg.cx := "0",
        svg.cy := "0",
        svg.r := s"$radius",
        svg.className := "hexagon-center-circle"
      ),
      svg.text(
        svg.x := "0",
        svg.y := "0",
        svg.fontSize := s"$radius",
        svg.className := "hexagon-center-number",
        content.number.map(_.toString).getOrElse("")
      )
    )

  private val svgImages: Element =
    svg.svg(
      svg.defs(
        for (terrain, path) <- ContextMap.resources.toList
        yield svg.pattern(
          svg.idAttr := terrain.toImgId,
          svg.width := "100%",
          svg.height := "100%",
          svg.patternContentUnits := "objectBoundingBox",
          svg.image(
            svg.href := path,
            svg.width := "1",
            svg.height := "1",
            svg.preserveAspectRatio := "none"
          )
        )
      )
    )
