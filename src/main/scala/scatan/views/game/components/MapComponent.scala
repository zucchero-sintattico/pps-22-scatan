package scatan.views.game.components

import com.raquo.laminar.api.L.*
import scatan.model.GameMap
import scatan.model.components.UnproductiveTerrain
import scatan.model.map.{Hexagon, TileContent}
import scatan.views.utils.Coordinates
import scatan.views.utils.Coordinates.center
import com.raquo.laminar.nodes.ReactiveSvgElement
import org.scalajs.dom.SVGGElement
import org.scalajs.dom
import scatan.model.components.Terrain
import scatan.model.components.ResourceType.*
import scatan.model.components.UnproductiveTerrain.*

/** A component to display the map of hexagons.
  */
object MapComponent:

  /** An anti corruption layer that maps the game map concept to the view ones.
    */
  private object MapContextMapping:
    /** Resources images.
      */
    val resources: Map[Terrain, String] = Map(
      Wood -> "res/img/hexagonal/wood.jpg",
      Sheep -> "res/img/hexagonal/sheep.jpg",
      Wheat -> "res/img/hexagonal/wheat.jpg",
      Rock -> "res/img/hexagonal/ore.jpg",
      Brick -> "res/img/hexagonal/clay.jpg",
      Desert -> "res/img/hexagonal/desert.jpg",
      Sea -> "res/img/hexagonal/water.jpg"
    )

    extension (terrain: Terrain) def toImgId: String = s"img-${terrain.toString.toLowerCase}"

  import MapContextMapping.*

  given hexSize: Int = 100
  val radius = hexSize / 4
  val svgCornersPoints: String =
    (for
      i <- 0 to 5
      angleDeg = 60 * i + 30
      angleRad = Math.PI / 180 * angleDeg
      x = hexSize * math.cos(angleRad)
      y = hexSize * math.sin(angleRad)
    yield s"$x,$y").mkString(" ")
  val layersToCanvasSize: Int => Int = x => (2 * x * hexSize) + 50

  type LaminarElement = Modifier[ReactiveSvgElement[dom.svg.Element]]

  type MapElement = GameMap ?=> Element
  private def gameMap(using GameMap): GameMap = summon[GameMap]
  private def contentOf(hex: Hexagon)(using GameMap): TileContent = gameMap.toContent(hex)

  /** A map compoment composed by hexagons.
    * @return
    *   the component.
    */
  def map: MapElement =
    mapContainer(
      for hex <- gameMap.tiles.toList
      yield svgHexagonWithNumber(hex)
    )

  /** Wrap the elements inside the container for map.
    * @param elements
    *   to put inside the container.
    * @return
    *   elements wrapped.
    */
  private[components] def mapContainer(
      elements: LaminarElement*
  ): MapElement =
    val canvasSize = layersToCanvasSize(gameMap.totalLayers)
    svg.svg(
      svgImages,
      svg.viewBox := s"-${canvasSize} -${canvasSize} ${2 * canvasSize} ${2 * canvasSize}",
      elements
    )

  private def svgHexagonWithNumber(hex: Hexagon): MapElement =
    svgHexagon(hex, circularNumber(hex))

  /** A svg representation of hexagon with terrain.
    * @param hex,
    *   the hexagon
    * @return
    *   the svg hexagon.
    */
  private[components] def svgHexagon(hex: Hexagon, elements: LaminarElement*): MapElement =
    val Coordinates(x, y) = hex.center
    svg.g(
      svg.transform := s"translate($x, $y)",
      svg.polygon(
        svg.points := svgCornersPoints,
        svg.cls := "hexagon",
        svg.fill := s"url(#${contentOf(hex).terrain.toImgId})"
      ),
      elements
    )

  /** A svg representation of a circular number.
    * @param hex,
    *   the hexagon
    * @return
    *   the svg circular number.
    */
  private[components] def circularNumber(
      hex: Hexagon,
      elements: LaminarElement*
  ): MapElement =
    contentOf(hex).terrain match
      case UnproductiveTerrain.Sea => svg.g()
      case _ =>
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
            contentOf(hex).number.map(_.toString).getOrElse("")
          ),
          elements
        )

  private val svgImages: Element =
    svg.svg(
      svg.defs(
        for (terrain, path) <- resources.toList
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
