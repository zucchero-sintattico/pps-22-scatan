package scatan.example.view

import scatan.mvc.lib.{ScalaJS, View}

trait HomeView extends View:
  def onCounterUpdated(counter: Int): Unit
