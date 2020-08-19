import sbt.ThisBuild


lazy val root = (project in file(".")).settings(
).aggregate(commons,grounds)

ThisBuild / organization := "nl.uva.cci.rs"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.3"

ThisBuild / resolvers += Resolver.mavenLocal
ThisBuild / resolvers += Resolver.bintrayRepo("pika-lab","tuprolog")
ThisBuild / resolvers += Resolver.bintrayRepo("uva-cci","script-cc-grammars")


ThisBuild / libraryDependencies += "nl.uva.sne.cci" % "parser" % "0.2.5"
ThisBuild / libraryDependencies += "nl.uva.sne.cci" % "scala-generator" % "0.2.5"
ThisBuild / libraryDependencies += "nl.uva.sne.cci" %% "stylaport" % "0.1.0-SNAPSHOT"

//ThisBuild / libraryDependencies += "eflint" %% "scala-server" % "0.1.2" exclude("ch.qos.logback", "logback-classic")

ThisBuild / libraryDependencies += "org.scalatest" %% "scalatest" % "3.3.0-SNAP2" % Test

ThisBuild / libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
ThisBuild / libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.7"
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.7"
ThisBuild / libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.25"
ThisBuild / libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.25"
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.7"
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.12"
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.12"
ThisBuild / libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0"

// https://mvnrepository.com/artifact/com.lihaoyi/ujson
ThisBuild / libraryDependencies += "com.lihaoyi" %% "ujson" % "1.2.0"


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


lazy val commons: Project = (project in file("commons")).settings(
  assemblyMergeStrategy in assembly := {
    case PathList("org", "jline", xs @ _*)         => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
    case "application.conf"                            => MergeStrategy.concat
    case "unwanted.txt"                                => MergeStrategy.discard
    case x if x.endsWith("module-info.class")         => MergeStrategy.discard
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)

lazy val flint: Project = (project in file("flint")).settings(
).dependsOn(commons).aggregate(commons)

lazy val grounds: Project = (project in file("grounds")).settings(
  mainClass in (Compile, run) := Some("scriptcc.Main"),
  mainClass in assembly := Some("scriptcc.Main"),
  assemblyMergeStrategy in assembly := {
    case PathList("org", "jline", xs @ _*)         => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
    case "application.conf"                            => MergeStrategy.concat
    case "unwanted.txt"                                => MergeStrategy.discard
    case x if x.endsWith("module-info.class")          => MergeStrategy.discard
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }

).dependsOn(commons).aggregate(commons)





