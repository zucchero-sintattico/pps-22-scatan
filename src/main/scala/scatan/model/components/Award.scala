package scatan.model.components

import scatan.model.game.config.ScatanPlayer

/** Type of possible awards.
  */
enum AwardType:
  case LongestRoad
  case LargestArmy

/** An award
  */
final case class Award(awardType: AwardType)

/** The assigned awards to the current holder player and the number of points.
  */
type Awards = Map[Award, Option[(ScatanPlayer, Int)]]

object Awards:
  def empty: Awards =
    Map(
      Award(AwardType.LargestArmy) -> Option.empty[(ScatanPlayer, Int)],
      Award(AwardType.LongestRoad) -> Option.empty[(ScatanPlayer, Int)]
    )
