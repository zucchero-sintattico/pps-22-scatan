package scatan.model.game.config

/** A player in a game of Scatan.
  */
trait ScatanPlayer:
  def name: String

object ScatanPlayer:
  def apply(name: String): ScatanPlayer = ScatanPlayerImpl(name)

private final case class ScatanPlayerImpl(name: String) extends ScatanPlayer
