package scatan.model

import scatan.BaseTest
import scatan.Pages
import game.Game

class ApplicationStateTest extends BaseTest:

  "An ApplicationState" should "exists" in {
    val applicationState: ApplicationState = ApplicationState()
  }

  it should "not have a game initially" in {
    val applicationState: ApplicationState = ApplicationState()
    applicationState.game should be(Option.empty[Game])
  }

  it should "allow to create a game" in {
    val applicationState: ApplicationState = ApplicationState()
    val applicationState2 = applicationState.createGame("Player 1", "Player 2", "Player 3", "Player 4")
    applicationState2.game should not be (Option.empty[Game])
  }

  it should "allow to create a game with 3 players" in {
    val applicationState: ApplicationState = ApplicationState()
    val applicationState2 = applicationState.createGame("Player 1", "Player 2", "Player 3")
    applicationState2.game should not be (Option.empty[Game])
  }

  it should "allow to create a game with 4 players" in {
    val applicationState: ApplicationState = ApplicationState()
    val applicationState2 = applicationState.createGame("Player 1", "Player 2", "Player 3", "Player 4")
    applicationState2.game should not be (Option.empty[Game])
  }

  it should "not allow to create a game with less than 3 players" in {
    val applicationState: ApplicationState = ApplicationState()
    for n <- 0 to 2
    yield assertThrows[IllegalArgumentException] {
      applicationState.createGame((1 to n).map(i => s"Player $i")*)
    }
  }

  it should "not allow to create a game with more than 4 players" in {
    val applicationState: ApplicationState = ApplicationState()
    for n <- 5 to 10
    yield assertThrows[IllegalArgumentException] {
      applicationState.createGame((1 to n).map(i => s"Player $i")*)
    }
  }
