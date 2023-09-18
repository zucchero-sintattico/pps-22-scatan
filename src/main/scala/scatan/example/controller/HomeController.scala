package scatan.example.controller

import scatan.mvc.lib.Controller

trait HomeController extends Controller:
  def counter: Int
  def increment(): Unit
