package scatan.model.components

import scala.annotation.targetName

object Listable:
  extension (count: Int)
    /** Small DSL to create a list of something.
      *
      * @param count
      * @return
      */
    @targetName("listOf")
    final def *[T](elem: T): Seq[T] = List.fill(count)(elem)

/** Unproductive terrain.
  */
enum UnproductiveTerrain:
  case Desert, Sea

type Terrain = ResourceType | UnproductiveTerrain
