name := "scala_nio_server"

version := "1.0"

scalaVersion := "2.13.11"

libraryDependencies += "ch.qos.logback" %  "logback-classic" % "1.1.7"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.10" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test"
libraryDependencies += "org.scalatestplus" %% "mockito-4-11" % "3.2.17.0" % "test"