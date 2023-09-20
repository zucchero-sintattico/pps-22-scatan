package scatan.model

import scatan.BaseTest

class ActionTest extends BaseTest:
  "An Action" should "be roll" in {
    Action.Roll shouldBe Action.Roll
  }

  it should "be roll seven" in {
    Action.RollSeven shouldBe Action.RollSeven
  }

  it should "be build" in {
    Action.Build shouldBe Action.Build
  }

  it should "be buy development card" in {
    Action.BuyDevelopmentCard shouldBe Action.BuyDevelopmentCard
  }

  it should "be play development card" in {
    Action.PlayDevelopmentCard shouldBe Action.PlayDevelopmentCard
  }

  it should "be trade" in {
    Action.Trade shouldBe Action.Trade
  }

  it should "be next turn" in {
    Action.NextTurn shouldBe Action.NextTurn
  }

class PhaseTest extends BaseTest:
  "A Phase" should "be initial" in {
    Phase.Initial shouldBe Phase.Initial
  }

  it should "be PlaceRobber" in {
    Phase.PlaceRobber shouldBe Phase.PlaceRobber
  }

  it should "be StoleCard" in {
    Phase.StoleCard shouldBe Phase.StoleCard
  }

  it should "be Playing" in {
    Phase.Playing shouldBe Phase.Playing
  }

  it should "be End" in {
    Phase.End shouldBe Phase.End
  }

  it should "have allowed actions" in {
    Phase.Initial.allowedActions shouldBe Set(Action.Roll, Action.RollSeven)
  }

  it should "have allowed actions for PlaceRobber" in {
    Phase.PlaceRobber.allowedActions shouldBe Set(Action.PlaceRobber)
  }

  it should "have allowed actions for StoleCard" in {
    Phase.StoleCard.allowedActions shouldBe Set(Action.StoleCard)
  }

  it should "have allowed actions for Playing" in {
    Phase.Playing.allowedActions shouldBe Set(
      Action.Build,
      Action.BuyDevelopmentCard,
      Action.PlayDevelopmentCard,
      Action.Trade,
      Action.End
    )
  }

class TurnTest extends BaseTest:

  "A turn" should "have a number" in {
    val turn = Turn(1, Player("a"))
    turn.number shouldBe 1
  }

  it should "not allow to have a number less than 1" in {
    assertThrows[IllegalArgumentException] {
      Turn(0, Player("a"))
    }
  }

  it should "have a player" in {
    val turn = Turn(1, Player("a"))
    turn.player shouldBe Player("a")
  }

  it should "not allow to have a player with an empty name" in {
    assertThrows[IllegalArgumentException] {
      Turn(1, Player(""))
    }
  }

  it should "have a phase" in {
    val turn = Turn(1, Player("a"))
    turn.phase shouldBe Phase.Initial
  }

class NewGameTest extends BaseTest:

  val players = Seq(Player("a"), Player("b"), Player("c"), Player("d"))

  "A game" should "have a turn" in {
    val game = Game(players)
    game.currentTurn shouldBe Turn(1, players(0))
  }

  it should "be possible to know if the game is over" in {
    val game = Game(players)
    game.isOver shouldBe false
  }

  it should "have a next turn" in {
    val game = Game(players)
    game.nextTurn.currentTurn shouldBe Turn(2, players(1))
  }

  it should "do a circular turn" in {
    val gameWith4Players = Game(players)
    val playersTurnIterator = Iterator.iterate(gameWith4Players)(_.nextTurn).map(_.currentPlayer)
    val playersIterator = Iterator.continually(players).flatten
    playersTurnIterator.take(100).toList shouldBe playersIterator.take(100).toList
  }

  it should "not allow to change turn if the game is over" in {
    val endedGame = Game(
      players = Seq(Player("a"), Player("b"), Player("c"), Player("d")),
      currentTurn = Turn(1, Player("a")),
      isOver = true
    )
    assertThrows[IllegalStateException] {
      endedGame.nextTurn
    }
  }

/*
  it should "have a phase" in {
    val turn = Turn(1, Player("a"))
    turn.phase shouldBe PhaseType.Initial
  }

  "A turn phase" should "be initial" in {
    val turn = Turn(1, Player("a"))
    turn.phase shouldBe PhaseType.Initial
  }

class ActionTest extends BaseTest:

  "An action" should "be roll" in {
    ActionType.Roll.toString shouldBe "Roll"
  }

  it should "be build" in {
    ActionType.Build.toString shouldBe "Build"
  }

  it should "be place robber" in {
    ActionType.PlaceRobber.toString shouldBe "PlaceRobber"
  }

  it should "be stole card" in {
    ActionType.StoleCard.toString shouldBe "StoleCard"
  }

  it should "be buy development card" in {
    ActionType.BuyDevelopmentCard.toString shouldBe "BuyDevelopmentCard"
  }

  it should "be play a development card" in {
    ActionType.PlayDevelopmentCard.toString shouldBe "PlayDevelopmentCard"
  }

  it should "be a players trade" in {
    ActionType.PlayersTrade.toString shouldBe "PlayersTrade"
  }

  it should "be a bank trade" in {
    ActionType.BankTrade.toString shouldBe "BankTrade"
  }

class PhaseTest extends BaseTest:

  "A phase" should "be initial" in {
    PhaseType.Initial.toString shouldBe "Initial"
  }

 */
