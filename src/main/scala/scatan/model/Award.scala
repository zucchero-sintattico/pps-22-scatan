package scatan.model

enum AwardType:
  case LongestRoad
  case LargestArmy

final case class Award(awardType: AwardType)

type Awards = Map[Award, Option[Player]]

object Award:
  def empty(): Awards =
    Map(
      Award(AwardType.LargestArmy) -> Option.empty[Player],
      Award(AwardType.LongestRoad) -> Option.empty[Player]
    )
