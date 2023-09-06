package scatan.example.view

import scatan.mvc.lib.{ScalaJSView, View}

trait HomeView extends View:
  def onCounterUpdated(counter: Int): Unit
