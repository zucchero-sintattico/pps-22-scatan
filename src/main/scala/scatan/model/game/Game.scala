package scatan.model.game

import scatan.model.game.GameRuleDSL.GameRuleDSLConfiguration

trait Game[State, P, A <: Action[State]](gameRules: GameRuleDSLConfiguration[State, P, A]):
  def players: Seq[Player]
  def turn: Turn
  def phase: P
  def state: State
  def isOver: Boolean = gameRules.isOver.exists(_.apply(state))
  def canPlay(a: A): Boolean = gameRules.phasesMap(phase).isDefinedAt(a)
  def play(action: A): Game[State, P, A]
  def nextTurn(): Game[State, P, A]

object Game:
  def apply[State, P, A <: Action[State]](
      players: Seq[Player]
  )(using
      gameRuleDSL: GameRuleDSL[State, P, A]
  ): Game[State, P, A] =
    require(gameRuleDSL.configuration.playersSizes contains players.size, "Invalid number of players")
    GameImpl(
      players,
      Turn(1, players.head),
      gameRuleDSL.configuration.initialPhase.get,
      gameRuleDSL.configuration.initialState.get
    )

private final case class GameImpl[State, P, A <: Action[State]](
    players: Seq[Player],
    turn: Turn,
    phase: P,
    state: State
)(using
    gameRuleDSL: GameRuleDSL[State, P, A]
) extends Game[State, P, A](gameRuleDSL.configuration):
  private val gameConfig = gameRuleDSL.configuration

  def nextTurn(): Game[State, P, A] =
    if phase == gameConfig.endingPhase.get then
      this.copy(
        turn = turn.next(players),
        phase = gameConfig.initialPhase.get
      )
    else this

  def play(action: A): Game[State, P, A] = action match
    case _ if !canPlay(action) =>
      this
    case _ =>
      val nextPhase = gameConfig.phasesMap(phase)(action)
      val newStatus = action.apply(state)
      GameImpl(
        players,
        turn,
        nextPhase,
        newStatus
      )
