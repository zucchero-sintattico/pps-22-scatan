package scatan.model.scatangame

import scatan.model.game.Player

enum AwardType:
  case LongestRoad
  case LargestArmy

final case class Award(awardType: AwardType)

type Awards = Map[Award, Option[(Player, Int)]]

object Award:
  def empty(): Awards =
    Map(
      Award(AwardType.LargestArmy) -> Option.empty[(Player, Int)],
      Award(AwardType.LongestRoad) -> Option.empty[(Player, Int)]
    )
