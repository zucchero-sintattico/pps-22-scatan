package scatan.model.game

import scatan.model.components.DevelopmentType.*
import scatan.model.components.{DevelopmentCard, DevelopmentType}

import scala.util.Random

extension (int: Int)
  def *(developmentType: DevelopmentType): DevelopmentCardsDeck =
    Seq.fill(int)(DevelopmentCard(developmentType))

type DevelopmentCardsDeck = Seq[DevelopmentCard]
object DevelopmentCardsDeck:

  def defaultOrdered: DevelopmentCardsDeck =
    14 * Knight ++
      5 * VictoryPoint ++
      2 * RoadBuilding ++
      2 * YearOfPlenty ++
      2 * Monopoly

  def shuffled: DevelopmentCardsDeck =
    Random.shuffle(defaultOrdered)
