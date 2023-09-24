package scatan.model.game

trait Action[State](val effect: State => State):
  def apply(game: State): State = effect(game)
