package scatan.model

import scatan.BaseTest

class TurnTest extends BaseTest:

  "A game" should "have a turn" in {
    val game = Game(Seq(Player("a"), Player("b"), Player("c"), Player("d")))
    game.currentTurn shouldBe Turn(1, Player("a"))
  }

  it should "have a next turn" in {
    val game = Game(Seq(Player("a"), Player("b"), Player("c"), Player("d")))
    game.nextTurn.currentTurn shouldBe Turn(2, Player("b"))
  }

  it should "do a circular turn" in {
    val gameWith4Players = Game(Seq(Player("a"), Player("b"), Player("c"), Player("d")))
    val playersTurnIterator = Iterator.iterate(gameWith4Players)(_.nextTurn).map(_.currentPlayer)
    val playersIterator = Iterator.continually(Seq(Player("a"), Player("b"), Player("c"), Player("d"))).flatten
    playersTurnIterator.take(100).toList shouldBe playersIterator.take(100).toList
  }

  "A turn" should "have a number" in {
    val turn = Turn(1, Player("a"))
    turn.number shouldBe 1
  }

  it should "have a player" in {
    val turn = Turn(1, Player("a"))
    turn.player shouldBe Player("a")
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
