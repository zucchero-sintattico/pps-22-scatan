ThisBuild / scalaVersion := "3.3.0"

val scalaTest = "org.scalatest" %% "scalatest" % "3.2.16" % "test"

lazy val scatan = (project in file("."))
  .settings(
    name := "Scatan"
    ,libraryDependencies ++= Seq(scalaTest)
  )