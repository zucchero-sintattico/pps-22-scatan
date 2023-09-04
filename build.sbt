ThisBuild / scalaVersion := "3.3.0"

lazy val scatan = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "Scatan",
    scalaVersion := "3.3.0",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.3.0-SNAP4" % Test,
      "org.scala-js" %%% "scalajs-dom" % "2.2.0",
      "com.raquo" %%% "laminar" % "16.0.0"
    ),
    scalaJSUseMainModuleInitializer := true
  )
