package scatan.lib.game

object EmptyDomain:
  type EmptyDomainRules = scatan.lib.game.Rules[State, Phases, Steps, Actions, Player]
  case class State()
  case class Player(name: String)
  enum Phases:
    case Game
  enum Steps:
    case Initial
  enum Actions:
    case StartGame

  def rules = Rules[State, Phases, Steps, Actions, Player](
    initialStateFactory = (_) => State(),
    initialPhase = Phases.Game,
    initialSteps = Map(Phases.Game -> Steps.Initial),
    actions = Map(GameStatus(Phases.Game, Steps.Initial) -> Map(Actions.StartGame -> Steps.Initial)),
    allowedPlayersSizes = Set(2, 3, 4),
    turnIteratorFactories = Map(Phases.Game -> (_.iterator)),
    nextPhase = Map(Phases.Game -> Phases.Game),
    endingSteps = Map(Phases.Game -> Steps.Initial),
    winner = (_ => None)
  )
