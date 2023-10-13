package scatan.lib.mvc

import scatan.lib.mvc.application.NavigableApplication

/** A singleton object that manages the currently running application. It is used to start the application and to
  * navigate to a new route. It is also used to navigate back to the previous route.
  */
object NavigableApplicationManager:
  private var _application: Option[NavigableApplication[?, ?]] = None

  def startApplication[Route](application: NavigableApplication[?, Route], initialRoute: Route): Unit =
    _application = Some(application)
    application.show(initialRoute)

  def navigateTo[Route](route: Route): Unit =
    _application.foreach(_.asInstanceOf[NavigableApplication[?, Route]].show(route))

  def navigateBack(): Unit =
    _application.foreach(_.back())
