package scriptcc

import agentfactory.FactoryManager
import akka.actor.typed
import akka.actor.typed.ActorSystem
import com.typesafe.config.ConfigFactory
import infrastructure.{AgentRequest, AgentRequestMessage, IMessage, MAS}

import scala.io.Source
import scala.language.implicitConversions

object MainRunner {

  def main(args: Array[String]): Unit = {

    val system = new scriptcc.ScriptRunner
    system.createMas("/home/msotafa/IdeaProjects/actor-playgrounds/src/test/asl/input.json",verbose = false)

  }

}
