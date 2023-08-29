ThisBuild / scalaVersion := "3.3.0"

val scalaTest = "org.scalatest" %% "scalatest" % "3.2.16" % "test"
//val scalaDom = "org.scala-js" %% "scalajs-dom" % "2.2.0"
//val laminar = "com.raquo" %%% "laminar" % "16.0.0"

lazy val scatan = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "Scatan"
    , libraryDependencies ++= Seq(scalaTest
      , "org.scala-js" %%% "scalajs-dom" % "2.2.0"
      , "com.raquo" %%% "laminar" % "16.0.0")
    , scalaJSUseMainModuleInitializer := true
  )

