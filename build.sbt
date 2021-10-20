import sbt.{ThisBuild, file}


licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0"))
lazy val AkkaVersion = "2.6.10"

ThisBuild / organization := "nl.uva.sne.cci"
ThisBuild / version      := "0.2.18"
ThisBuild / scalaVersion := "2.13.3"
testOptions in Test += Tests.Argument("-oD")
//ThisBuild / resolvers += Resolver.mavenLocal

//ThisBuild / resolvers += Resolver.bintrayRepo("pika-lab","tuprolog")
//ThisBuild / resolvers += Resolver.bintrayRepo("uva-cci","script-cc-grammars")
//ThisBuild / resolvers += Resolver.bintrayRepo("uva-cci","styla-prolog")
ThisBuild / resolvers += Resolver.githubPackages("mostafamohajeri", "scriptcc-translator")
ThisBuild / resolvers += Resolver.githubPackages("mostafamohajeri", "styla")
githubTokenSource := TokenSource.GitConfig("github.token") || TokenSource.Environment("GITHUB_TOKEN")


ThisBuild / libraryDependencies += "nl.uva.sne.cci" % "parser" % "2.23"
ThisBuild / libraryDependencies += "nl.uva.sne.cci" % "scala-generator" % "2.23"
ThisBuild / libraryDependencies += "nl.uva.sne.cci" %% "styla" % "0.2.2"

ThisBuild / libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.3" % Test
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test

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
 (agentScriptCC / agentScriptCCPath) in Compile :=  (baseDirectory.value / "src" / "test" / "asl"),


 Compile / sourceGenerators += (Compile / agentScriptCC).taskValue,
  skip in publish := true,
  jacocoReportSettings := JacocoReportSettings(
  "Jacoco Coverage Report",
  None,
  JacocoThresholds(),
  Seq(JacocoReportFormats.ScalaHTML),
  "utf-8"),
  githubTokenSource := TokenSource.GitConfig("github.token") || TokenSource.Environment("GITHUB_TOKEN"),


).dependsOn(agent_script_commons,agent_script_grounds,agent_script_serialize).aggregate(agent_script_commons,agent_script_grounds,agent_script_serialize)

lazy val agent_script_commons: Project = (project in file("commons")).settings(
//  bintrayOrganization := Some("uva-cci"),
//  bintrayRepository := "agent-script-playgrounds",
//  bintrayOmitLicense := true,
  assemblyMergeStrategy in assembly := {
    case PathList("org", "jline", xs @ _*)         => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
    case "application.conf"                            => MergeStrategy.concat
    case "unwanted.txt"                                => MergeStrategy.discard
    case x if x.endsWith("module-info.class")         => MergeStrategy.discard
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  },
  name := "agentscript-commons",
  githubTokenSource := TokenSource.GitConfig("github.token") || TokenSource.Environment("GITHUB_TOKEN"),
  githubOwner := "mostafamohajeri",
  githubRepository := "agentscript"
)

lazy val agent_script_serialize: Project = (project in file("serialize")).settings(
//  bintrayOrganization := Some("uva-cci"),
//  bintrayRepository := "agent-script-playgrounds",
//  bintrayOmitLicense := true,
  name := "agentscript-serialize",
  githubTokenSource := TokenSource.GitConfig("github.token") || TokenSource.Environment("GITHUB_TOKEN"),
  githubOwner := "mostafamohajeri",
  githubRepository := "agentscript"
).dependsOn(agent_script_commons).aggregate(agent_script_commons)

lazy val agent_script_grounds: Project = (project in file("grounds")).enablePlugins(AgentScriptCCPlugin)

  .settings(
//  bintrayOrganization := Some("uva-cci"),
//  bintrayRepository := "agent-script-playgrounds",
//  bintrayOmitLicense := true,
    (agentScriptCC / agentScriptCCPath) in Compile :=  (baseDirectory.value / "src" / "main" / "asl" / "agere2020" / "ring"),
    Compile / sourceGenerators += (Compile / agentScriptCC).taskValue,
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
    name := "agentscript-grounds",
    githubTokenSource := TokenSource.GitConfig("github.token") || TokenSource.Environment("GITHUB_TOKEN"),
    githubOwner := "mostafamohajeri",
    githubRepository := "agentscript"
).dependsOn(agent_script_commons).aggregate(agent_script_commons)





