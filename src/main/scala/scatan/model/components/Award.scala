package scatan.model.components

import scatan.model.game.config.ScatanPlayer

enum AwardType:
  case LongestRoad
  case LargestArmy

final case class Award(awardType: AwardType)

type Awards = Map[Award, Option[(ScatanPlayer, Int)]]

object Award:
  def empty(): Awards =
    Map(
      Award(AwardType.LargestArmy) -> Option.empty[(ScatanPlayer, Int)],
      Award(AwardType.LongestRoad) -> Option.empty[(ScatanPlayer, Int)]
    )
