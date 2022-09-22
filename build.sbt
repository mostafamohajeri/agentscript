import sbt.{ThisBuild, file, url}
import sbtassembly.AssemblyPlugin.autoImport.assemblyMergeStrategy
import scriptcc.AgentScriptCCPlugin.autoImport.agentScriptCC


licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0"))
lazy val AkkaVersion = "2.6.17"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/mostafamohajeri/agentscript"),
    "scm:git@github.com:mostafamohajeri/agentscript.git"
  )
)


ThisBuild / developers := List(
  Developer(
    id    = "mostafamohajeri",
    name  = "Mostafa",
    email = "m.mohajeriparizi@uva.nl",
    url   = url("https://github.com/mostafamohajeri/")
  )
)

ThisBuild / description := "AgentScript framework"
ThisBuild / homepage := Some(url("https://github.com/mostafamohajeri/agentscript"))


ThisBuild / organization := "io.github.mostafamohajeri"
ThisBuild / organizationName := "CCI Group"
ThisBuild / organizationHomepage := Some(url("https://cci-research.nl/"))
ThisBuild / version      := "0.45"
ThisBuild / scalaVersion := "2.13.7"
Test / testOptions  += Tests.Argument("-oD")


ThisBuild / libraryDependencies += "io.github.mostafamohajeri" % "agentscript-parser" % "2.50"
ThisBuild / libraryDependencies += "io.github.mostafamohajeri" % "agentscript-scala-generator" % "2.50"
ThisBuild / libraryDependencies += "io.github.mostafamohajeri" %% "styla" % "0.2.3"

ThisBuild / libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.3" % Test
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test

ThisBuild / libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
ThisBuild / libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion
ThisBuild / libraryDependencies += "org.slf4j" % "slf4j-api" % "2.0.2"
ThisBuild / libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "2.0.2"
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaVersion
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.2.10"
ThisBuild / libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.10"
ThisBuild / libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0"


ThisBuild / libraryDependencies += "com.lihaoyi" %% "ujson" % "1.2.0"


lazy val agent_script_playgrounds = (project in file(".")).enablePlugins(AgentScriptCCPlugin).settings(
Compile / agentScriptCC / agentScriptCCPath:=  (baseDirectory.value / "src" / "test" / "asl"),


 Compile / sourceGenerators += (Compile / agentScriptCC).taskValue,
  publish / skip := true,
  jacocoReportSettings := JacocoReportSettings(
  "Jacoco Coverage Report",
  None,
  JacocoThresholds(),
  Seq(JacocoReportFormats.ScalaHTML),
  "utf-8"),
//  githubTokenSource := TokenSource.GitConfig("github.token") || TokenSource.Environment("GITHUB_TOKEN"),


).dependsOn(agent_script_commons,agent_script_grounds).aggregate(agent_script_commons,agent_script_grounds)

lazy val agent_script_commons: Project = (project in file("commons")).settings(
//  bintrayOrganization := Some("uva-cci"),
//  bintrayRepository := "agent-script-playgrounds",
//  bintrayOmitLicense := true,
  assembly / assemblyMergeStrategy := {
    case PathList("org", "jline", xs @ _*)         => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
    case "application.conf"                            => MergeStrategy.concat
    case "unwanted.txt"                                => MergeStrategy.discard
    case x if x.endsWith("module-info.class")         => MergeStrategy.discard
    case x =>
      val oldStrategy = (assembly / assemblyMergeStrategy).value
      oldStrategy(x)
  },
  licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0")),
  name := "agentscript-commons",
  pomIncludeRepository := { _ => false },
  publishTo := {
    val nexus = "https://s01.oss.sonatype.org/"
    if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
    else Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }

)

//lazy val agent_script_serialize: Project = (project in file("bb.serialize")).settings(
////  bintrayOrganization := Some("uva-cci"),
////  bintrayRepository := "agent-script-playgrounds",
////  bintrayOmitLicense := true,
//  name := "agentscript-bb.serialize",
//  githubTokenSource := TokenSource.GitConfig("github.token") || TokenSource.Environment("GITHUB_TOKEN"),
//  githubOwner := "mostafamohajeri",
//  githubRepository := "agentscript"
//).dependsOn(agent_script_commons).aggregate(agent_script_commons)

lazy val agent_script_grounds: Project = (project in file("grounds")).enablePlugins(AgentScriptCCPlugin)
  .settings(
    Compile / agentScriptCC / agentScriptCCPath :=  (baseDirectory.value / "src" / "main" / "asl" / "agere2020" / "ring"),
    Compile / sourceGenerators += (Compile / agentScriptCC).taskValue,
    Compile / mainClass :=  Some("scriptcc.Main"),
    run / mainClass :=  Some("scriptcc.Main"),
    assembly / mainClass :=  Some("scriptcc.Main"),
    assembly / assemblyMergeStrategy:= {
    case PathList("org", "jline", xs @ _*)         => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
    case "application.conf"                            => MergeStrategy.concat
    case "unwanted.txt"                                => MergeStrategy.discard
    case x if x.endsWith("module-info.class")          => MergeStrategy.discard
    case x =>
      val oldStrategy = (assembly / assemblyMergeStrategy).value
      oldStrategy(x)
  },
    licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0")),
    name := "agentscript-grounds",
    pomIncludeRepository := { _ => false },
    publishTo := {
      val nexus = "https://s01.oss.sonatype.org/"
      if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
      else Some("releases" at nexus + "service/local/staging/deploy/maven2")
    }

).dependsOn(agent_script_commons).aggregate(agent_script_commons)





