name := "scala_nio_server"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "ch.qos.logback" %  "logback-classic" % "1.1.7"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
libraryDependencies += "org.mockito" % "mockito-core" % "1.10.19" % "test"