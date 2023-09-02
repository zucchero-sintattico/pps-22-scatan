ThisBuild / scalaVersion := "3.3.0"

lazy val scatan = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "Scatan",
    libraryDependencies ++= Seq(
      // Do not remove or change order
      "org.scalatest" %%% "scalatest" % "3.3.0-SNAP4" % Test,
      "org.scalatest" %% "scalatest" % "3.3.0-SNAP4" % Test,

      "org.scala-js" %%% "scalajs-dom" % "2.6.0",
      "com.raquo" %%% "laminar" % "16.0.0",
    ),
    scalaJSUseMainModuleInitializer := true
  )
