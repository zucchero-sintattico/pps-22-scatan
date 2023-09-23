package scatan.model

import scatan.BaseTest
import scatan.model.Game
import scatan.model.Player
import scatan.model.DevelopmentCard

class GameWithDevCardsTest extends BasicGameTest:

  "A Game with development cards" should "have empty development cards when game start" in {
    val game: Game = Game(players = threePlayers)
    game.developmentCardsOfPlayers(threePlayers.head) should be(Seq.empty[DevelopmentCard])
  }

  it should "allow to assign development cards" in {
    val initialGame: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val gameWithDevCardsAssigned = initialGame
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
      .assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    initialGame.developmentCardsOfPlayers(player1) should be(Seq.empty[DevelopmentCard])
    gameWithDevCardsAssigned.developmentCardsOfPlayers(player1) should be(
      Seq(DevelopmentCard(DevelopmentType.Knight), DevelopmentCard(DevelopmentType.Knight))
    )
  }

  it should "allow to consume development cards" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val game2 = game.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    game2.developmentCardsOfPlayers(player1) should be(Seq(DevelopmentCard(DevelopmentType.Knight)))
    game2.developmentCardsOfPlayers(player2) should be(Seq.empty[DevelopmentCard])
    val game3 = game2.consumeDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    game3.developmentCardsOfPlayers(player1) should be(Seq.empty[DevelopmentCard])
    game3.developmentCardsOfPlayers(player2) should be(Seq.empty[DevelopmentCard])
    val game4 = game3.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    game4.developmentCardsOfPlayers(player1) should be(Seq(DevelopmentCard(DevelopmentType.Knight)))
    game4.developmentCardsOfPlayers(player2) should be(Seq.empty[DevelopmentCard])
    val game5 = game4.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    game5.developmentCardsOfPlayers(player1) should be(
      Seq(DevelopmentCard(DevelopmentType.Knight), DevelopmentCard(DevelopmentType.Knight))
    )
    game5.developmentCardsOfPlayers(player2) should be(Seq.empty[DevelopmentCard])
    val game6 = game5.consumeDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    game6.developmentCardsOfPlayers(player1) should be(Seq(DevelopmentCard(DevelopmentType.Knight)))
    game6.developmentCardsOfPlayers(player2) should be(Seq.empty[DevelopmentCard])
    val game7 = game6.consumeDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    game7.developmentCardsOfPlayers(player1) should be(Seq.empty[DevelopmentCard])
    game7.developmentCardsOfPlayers(player2) should be(Seq.empty[DevelopmentCard])
  }

  it should "not allow to consume development cards if the player does not have it" in {
    val game: Game = Game(players = threePlayers)
    val player1 = threePlayers.head
    val player2 = threePlayers.tail.head
    val game2 = game.assignDevelopmentCard(player1, DevelopmentCard(DevelopmentType.Knight))
    game2.developmentCardsOfPlayers(player1) should be(Seq(DevelopmentCard(DevelopmentType.Knight)))
    game2.developmentCardsOfPlayers(player2) should be(Seq.empty[DevelopmentCard])
    val game3 = game2.consumeDevelopmentCard(player2, DevelopmentCard(DevelopmentType.Knight))
    game3.developmentCardsOfPlayers(player1) should be(Seq(DevelopmentCard(DevelopmentType.Knight)))
    game3.developmentCardsOfPlayers(player2) should be(Seq.empty[DevelopmentCard])
  }
