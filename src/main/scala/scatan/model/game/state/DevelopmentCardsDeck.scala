package scatan.model.game.state

import scatan.model.components.DevelopmentType.*
import scatan.model.components.{DevelopmentCard, DevelopmentType}

import scala.annotation.targetName
import scala.util.Random

extension (int: Int)
  @targetName("timesDevelopmentType")
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

  def shuffled(seed: Int = 1): DevelopmentCardsDeck =
    val random = Random(seed)
    defaultOrdered.sortBy(_ => random.nextDouble())
