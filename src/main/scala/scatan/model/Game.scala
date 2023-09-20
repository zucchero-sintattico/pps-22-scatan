package scatan.model

final case class Player(name: String)

@SuppressWarnings(Array("org.wartremover.warts.Throw"))
final case class Game(players: Seq[Player], awards: Awards):
  private val numberOfPlayers = players.size
  private val numberOfLayers = 2
  private val gameMap = GameMap(numberOfLayers)

  def awards(award: Award): Option[Player] = awards.get(award).flatten

  def assignAward(award: Award, player: Player): Game =
    val newAwards = awards.updated(award, Some(player))
    copy(awards = newAwards)

  if players.sizeIs < 3 then throw IllegalArgumentException("A game must have at least 3 players")
  if players.sizeIs > 4 then throw IllegalArgumentException("A game must have at most 4 players")

extension (game: Game)
  def currentPlayer: Player = game.players.head
  def withNextPlayer: Game = game.copy(players = game.players.tail :+ game.players.head)
