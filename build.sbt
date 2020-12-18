import sbt.{ThisBuild, file}


licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0"))
lazy val AkkaVersion = "2.6.10"

ThisBuild / organization := "nl.uva.sne.cci"
ThisBuild / version      := "0.2.0"
ThisBuild / scalaVersion := "2.13.3"

ThisBuild / resolvers += Resolver.mavenLocal

ThisBuild / resolvers += Resolver.bintrayRepo("pika-lab","tuprolog")
ThisBuild / resolvers += Resolver.bintrayRepo("uva-cci","script-cc-grammars")
ThisBuild / resolvers += Resolver.bintrayRepo("uva-cci","styla-prolog")


ThisBuild / libraryDependencies += "nl.uva.sne.cci" % "parser" % "0.2.11"
ThisBuild / libraryDependencies += "nl.uva.sne.cci" % "scala-generator" % "0.2.11"
ThisBuild / libraryDependencies += "nl.uva.sne.cci" %% "stylaport" % "0.1.2"

ThisBuild / libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.3" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test

ThisBuild / libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
ThisBuild / libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion
ThisBuild / libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.25"
ThisBuild / libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.25"
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaVersion
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.12"
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.12"
ThisBuild / libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0"


ThisBuild / libraryDependencies += "com.lihaoyi" %% "ujson" % "1.2.0"


lazy val agent_script_playgrounds = (project in file(".")).enablePlugins(AgentScriptCCPlugin).settings(
  (agentScriptCC / agentScriptCCPath) in Test := (baseDirectory.value / "src" / "test" / "asl"),
  Test / sourceGenerators += (Test / agentScriptCC).taskValue,
  skip in publish := true,

).dependsOn(agent_script_commons,agent_script_grounds).aggregate(agent_script_commons,agent_script_grounds)

lazy val agent_script_commons: Project = (project in file("commons")).settings(


  bintrayOrganization := Some("uva-cci"),
  bintrayRepository := "agent-script-playgrounds",
  bintrayOmitLicense := true,
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

  skip in publish := true

)

lazy val agent_script_grounds: Project = (project in file("grounds"))

  .settings(
  bintrayOrganization := Some("uva-cci"),
  bintrayRepository := "agent-script-playgrounds",
  bintrayOmitLicense := true,
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
  },

).dependsOn(agent_script_commons).aggregate(agent_script_commons)





