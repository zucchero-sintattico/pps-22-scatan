ThisBuild / scalaVersion := "3.3.0"

Compile / doc / target := file("scaladoc") // set output directory

lazy val scatan = (project in file("."))
  .settings(
    name := "Scatan"
  )