package scatan.model.game

trait Game[P, A <: Action](phasesMap: Map[P, PartialFunction[A, P]] = Map.empty):
  def players: Seq[Player]
  def turn: Turn
  def phase: P
  def isOver: Boolean
  def canPlay(a: A): Boolean = phasesMap(phase).isDefinedAt(a)
  def play(action: A): Game[P, A]
  def nextTurn(): Game[P, A]

object Game:
  def apply[P, A <: Action](
      players: Seq[Player],
      isOver: Boolean = false
  )(using
      gameRuleDSL: GameRuleDSL[P, A]
  ): Game[P, A] =
    require(gameRuleDSL.configuration.playersSizes contains players.size, "Invalid number of players")
    GameImpl(players, Turn(1, players.head), gameRuleDSL.configuration.initialPhase.get, isOver)

private final case class GameImpl[P, A <: Action](
    players: Seq[Player],
    turn: Turn,
    phase: P,
    isOver: Boolean = false
)(using
    gameRuleDSL: GameRuleDSL[P, A]
) extends Game[P, A](gameRuleDSL.configuration.phasesMap):
  private val gameConfig = gameRuleDSL.configuration

  def nextTurn(): Game[P, A] =
    if phase == gameConfig.endingPhase.get then
      this.copy(
        turn = turn.next(players),
        phase = gameConfig.initialPhase.get
      )
    else this

  def play(action: A): Game[P, A] = action match
    case _ if !canPlay(action) =>
      this
    case _ =>
      val nextPhase = gameConfig.phasesMap(phase)(action)
      val gameAfterAction = action.apply(this)
      GameImpl(
        gameAfterAction.players,
        gameAfterAction.turn,
        nextPhase
      )
