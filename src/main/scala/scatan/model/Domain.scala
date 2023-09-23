package scatan.model

import scatan.model.MyActions.RollDice
import scatan.model.map.Hexagon
import scatan.model.newaction.Action
import scatan.model.newgame.Game
import scatan.model.newplayer.Player

package newplayer:
  trait Player:
    def name: String

  object Player:
    def apply(name: String): Player = PlayerImpl(name)

  private final case class PlayerImpl(name: String) extends Player
end newplayer

package newaction:
  import scatan.model.newgame.Game

  trait Action(val effect: Game[?, ?] => Game[?, ?]):
    def apply[P, A <: Action](game: Game[P, A]): Game[P, A] = effect(game).asInstanceOf[Game[P, A]]

end newaction

package newturn:
  import scatan.model.newplayer.Player

  trait Turn:
    def number: Int
    def player: Player

  object Turn:
    def apply(number: Int, player: Player): Turn = TurnImpl(number, player)

  private final case class TurnImpl(number: Int, player: Player) extends Turn
end newturn

package newgame:
  import scatan.model.newaction.Action
  import scatan.model.newplayer.Player
  import scatan.model.newturn.Turn
  trait Game[P, A <: Action](phasesMap: Map[P, PartialFunction[A, P]] = Map.empty):
    def players: Seq[Player]
    def turn: Turn
    def phase: P
    def canPlay(a: A): Boolean = phasesMap(phase).isDefinedAt(a)
    def play(action: A): Game[P, A]

  object Game:
    def apply[P, A <: Action](using gameRuleDSL: GameRuleDSL[P, A])(players: Seq[Player]): Game[P, A] =
      GameImpl(players, Turn(1, players.head), gameRuleDSL.initialPhase.get)

  private final case class GameImpl[P, A <: Action](players: Seq[Player], turn: Turn, phase: P)(using
      gameRuleDSL: GameRuleDSL[P, A]
  ) extends Game[P, A](gameRuleDSL.phasesMap):
    def play(action: A): Game[P, A] =
      if canPlay(action) then
        val nextPhase = gameRuleDSL.phasesMap(phase)(action)
        this.copy(
          phase = nextPhase
        )
      else throw new IllegalArgumentException(s"Cannot play $action in phase ${phase}")
end newgame

trait GameRuleDSL[P, A <: Action]:

  var initialPhase: Option[P] = None
  def Start = new Start()
  class Start:
    def withPhase(phase: P): Unit = initialPhase = Some(phase)

  var phasesMap: Map[P, PartialFunction[A, P]] = Map.empty
  def When = this
  def in(phase: P) = new In(phase)
  class In(_phase: P):
    def phase(mapping: PartialFunction[A, P]): Unit =
      phasesMap = phasesMap.updated(_phase, mapping)

enum MyPhases:
  case Initial
  case PlaceRobber
  case StoleCard
  case Main

enum MyActions(effect: Game[?, ?] => Game[?, ?] = identity) extends Action(effect):
  case RollDice(result: Int)
  case PlaceRobberOnHexagon(hexagon: Hexagon)
  case StoleCardFromPlayer(player: Player)
  case Build
  case BuyDevelopmentCard
  case PlayDevelopmentCard(card: DevelopmentCard)
  case Trade
  case EndTurn

object CatanRules extends GameRuleDSL[MyPhases, MyActions]:
  import MyActions.*
  import MyPhases.*

  Start withPhase Initial

  When in Initial phase {
    case RollDice(7) => PlaceRobber
    case RollDice(_) => Main
  }

  When in PlaceRobber phase { case PlaceRobberOnHexagon(_) =>
    StoleCard
  }

  When in StoleCard phase { case StoleCardFromPlayer(_) =>
    Main
  }

  When in Main phase {
    case Build                  => Main
    case BuyDevelopmentCard     => Main
    case PlayDevelopmentCard(_) => Main
    case Trade                  => Main
    case EndTurn                => Initial
  }

@main def test =
  import scatan.model.newgame.Game
  import scatan.model.newplayer.Player

  given GameRuleDSL[MyPhases, MyActions] = CatanRules
  val players = Seq(Player("Alice"), Player("Bob"))
  val game: Game[MyPhases, MyActions] = Game(players)
  println(game)
  val gameAfterSeven = game.play(RollDice(7))
  println(gameAfterSeven)
  val gameAfterOtherRoll = game.play(RollDice(6))
  println(gameAfterOtherRoll)

/*
object Test:
  import Actions.*
  import scatan.model.newaction.Action
  import scatan.model.newgame.Game
  import scatan.model.newphase.Phase
  import scatan.model.newplayer.Player
  import scatan.model.newturn.Turn

  enum Actions(effect: Game[Actions] => Game[Actions] = identity) extends Action[Actions](effect):
    case BuildRoad extends Actions
    case BuildSettlement extends Actions
    case BuildCity extends Actions
    case BuyDevelopmentCard extends Actions
    case PlayDevelopmentCard extends Actions
    case Trade extends Actions
    case EndTurn extends Actions

  enum Phases(
      otherAllowedActions: Map[Class[? <: Actions], Phase[Actions]] = Map.empty
  ) extends Phase[Actions](Seq.empty, otherAllowedActions):
    case Initial
        extends Phases(
          otherAllowedActions = Map(
            BuildRoad -> Initial,
            BuildSettlement -> Initial,
            EndTurn -> Main
          )
        )

  given actionToClass: Conversion[Actions, Class[? <: Actions]] = _.getClass

  val game = Game(
    Seq(Player("Alice"), Player("Bob")),
    Turn(1, Player("Alice"), Phases.Initial)
  )


 */
