package scatan.lib

final case class Player(name: String):
  require(name.nonEmpty, "A player must have a non-empty name")

def Players(age: Int, players: Player*): Seq[Player] =
  require(players.sizeIs >= 3, "A game must have at least 3 players")
  require(players.sizeIs <= 4, "A game must have at most 4 players")
  players

@main def hello: Unit =
  Players(
    age = 42,
    Player("Alice"),
    Player("Bob"),
    Player("Carol")
  )
