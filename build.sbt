import sbt.Keys.libraryDependencies

lazy val root = (project in file("."))


ThisBuild / resolvers += Resolver.mavenLocal

// https://mvnrepository.com/artifact/org.scalatest/scalatest


libraryDependencies += "org.scalatest" %% "scalatest" % "3.3.0-SNAP2" % Test

// https://mvnrepository.com/artifact/com.lihaoyi/ujson
ThisBuild / libraryDependencies += "com.lihaoyi" %% "ujson" % "1.1.0"
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.5"
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.5"
// https://mvnrepository.com/artifact/com.lihaoyi/request
ThisBuild / libraryDependencies += "com.lihaoyi" %% "requests" % "0.6.2"


ThisBuild / libraryDependencies += "it.unibo.alice.tuprolog" % "2p-core" % "4.1.1"
ThisBuild / libraryDependencies += "it.unibo.alice.tuprolog" % "2p-parser" % "4.1.1"
ThisBuild / libraryDependencies += "it.unibo.alice.tuprolog" % "2p-presentation" % "4.1.1"
ThisBuild / libraryDependencies += "it.unibo.alice.tuprolog" % "2p-ui" % "4.1.1"

ThisBuild / organization := "nl.uva.cci.rs"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.10"

lazy val commons: Project = project in file("commons")

lazy val flint: Project = (project in file("flint")).settings(
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.3.0-SNAP2" % Test,
    // https://mvnrepository.com/artifact/com.lihaoyi/ujson
  libraryDependencies += "com.lihaoyi" %% "ujson" % "1.1.0",
  libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.5",
  libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.5",
  // https://mvnrepository.com/artifact/com.lihaoyi/requests
  libraryDependencies += "com.lihaoyi" %% "requests" % "0.6.2"
).dependsOn(commons).aggregate(commons)

lazy val grounds: Project = (project in file("grounds")).settings(
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.3.0-SNAP2" % Test,
  // https://mvnrepository.com/artifact/com.lihaoyi/ujson
  libraryDependencies += "com.lihaoyi" %% "ujson" % "1.1.0",
  libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.5",
  libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.5",
  // https://mvnrepository.com/artifact/com.lihaoyi/requests
  libraryDependencies += "com.lihaoyi" %% "requests" % "0.6.2",
  // https://mvnrepository.com/artifact/it.unibo.alice.tuprolog/2p-core
  mainClass in (Compile, run) := Some("Main")

).dependsOn(commons).aggregate(commons)





