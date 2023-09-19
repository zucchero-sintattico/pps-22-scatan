package scatan.lib.mvc

import scatan.lib.mvc.application.{Application, NavigableApplication}

object NavigableApplicationManager:
  private var _application: Option[NavigableApplication[?, ?]] = None

  def startApplication[Route](application: NavigableApplication[?, Route], initialRoute: Route): Unit =
    _application = Some(application)
    application.show(initialRoute)

  def navigateTo[Route](route: Route): Unit =
    _application.foreach(_.asInstanceOf[NavigableApplication[?, Route]].show(route))

  def navigateBack(): Unit =
    _application.foreach(_.back())
