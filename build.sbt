import sbt.Keys.{javaOptions, libraryDependencies}

lazy val root = (project in file(".")).aggregate(commons,grounds)




ThisBuild / resolvers += Resolver.bintrayRepo("pika-lab","tuprolog")
ThisBuild / resolvers += Resolver.mavenLocal


libraryDependencies += "org.scalatest" %% "scalatest" % "3.3.0-SNAP2" % Test


ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.7"
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.7"
ThisBuild / libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.25"
ThisBuild / libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.25"
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.7"
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.12"
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.12"
ThisBuild / libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0"

ThisBuild / libraryDependencies += "it.unibo.tuprolog" % "solve-jvm" % "0.11.1-dev07+c350ea70"
ThisBuild / libraryDependencies += "it.unibo.tuprolog" % "solve-classic-jvm" % "0.11.1-dev07+c350ea70"
ThisBuild / libraryDependencies += "it.unibo.tuprolog" % "core-jvm" % "0.11.1-dev07+c350ea70"
ThisBuild / libraryDependencies += "it.unibo.tuprolog" % "theory-jvm" % "0.11.1-dev07+c350ea70"
ThisBuild / libraryDependencies += "it.unibo.tuprolog" % "serialize-core-jvm" % "0.11.1-dev07+c350ea70"
ThisBuild / libraryDependencies += "it.unibo.tuprolog" % "serialize-theory-jvm" % "0.11.1-dev07+c350ea70"
ThisBuild / libraryDependencies += "it.unibo.tuprolog" % "parser-jvm" % "0.11.1-dev07+c350ea70"
ThisBuild / libraryDependencies += "it.unibo.tuprolog" % "parser-core-jvm" % "0.11.1-dev07+c350ea70"
ThisBuild / libraryDependencies += "it.unibo.tuprolog" % "parser-theory-jvm" % "0.11.1-dev07+c350ea70"
ThisBuild / libraryDependencies += "it.unibo.tuprolog" % "dsl-unify-jvm" % "0.11.1-dev07+c350ea70"
ThisBuild / libraryDependencies += "it.unibo.tuprolog" % "dsl-core-jvm" % "0.11.1-dev07+c350ea70"
ThisBuild / libraryDependencies += "it.unibo.tuprolog" % "dsl-theory-jvm" % "0.11.1-dev07+c350ea70"



ThisBuild / organization := "nl.uva.cci.rs"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.3"

lazy val commons: Project = project in file("commons")

lazy val flint: Project = (project in file("flint")).settings(


).dependsOn(commons).aggregate(commons)

lazy val grounds: Project = (project in file("grounds")).settings(
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.3.0-SNAP2" % Test,
  mainClass in (Compile, run) := Some("Main"),
).dependsOn(commons).aggregate(commons)





