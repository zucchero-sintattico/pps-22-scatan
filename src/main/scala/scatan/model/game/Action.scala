package scatan.model.game

trait Action(val effect: Game[?, ?] => Game[?, ?]):
  def apply[P, A <: Action](game: Game[P, A]): Game[P, A] = effect(game).asInstanceOf[Game[P, A]]
