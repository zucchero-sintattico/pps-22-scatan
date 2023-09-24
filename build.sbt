ThisBuild / scalaVersion := "3.3.0"

wartremoverWarnings ++= Warts.all
wartremoverWarnings -= Wart.Equals
wartremoverWarnings -= Wart.Overloading
wartremoverWarnings -= Wart.ImplicitParameter
wartremoverWarnings -= Wart.IterableOps
wartremoverWarnings -= Wart.DefaultArguments
wartremoverWarnings -= Wart.AsInstanceOf

lazy val scatan = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "Scatan",
    scalaVersion := "3.3.0",
    libraryDependencies ++= Seq(
      // Do not remove or change order
      "org.scalatest" %%% "scalatest" % "3.3.0-SNAP4" % Test,
      "org.scalatest" %% "scalatest" % "3.3.0-SNAP4" % Test,
      "org.scala-js" %%% "scalajs-dom" % "2.6.0",
      "com.raquo" %%% "laminar" % "16.0.0",
      "org.typelevel" %%% "cats-core" % "2.10.0",
      "org.scalatestplus" %%% "scalacheck-1-17" % "3.2.16.0" % Test
    ),
    scalaJSUseMainModuleInitializer := true
  )
