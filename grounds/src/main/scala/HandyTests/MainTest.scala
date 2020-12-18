package HandyTests

import agentfactory.FactoryManager
import akka.actor.typed
import akka.actor.typed.{ActorRefResolver, ActorSystem}
import infrastructure.{AgentRequest, AgentRequestMessage, IMessage, MAS}

import scala.language.implicitConversions
import std.converters._
import translation.Translator

import scala.language.implicitConversions

object MainTest {



//  val f = "/home/msotafa/IdeaProjects/actor-playgrounds/grounds/src/main/asl/agere2020/benchmark3/talker.asl"
//
//
//
//
//  val a = agents.talker.Agent
//  val translator: Translator = new Translator()
//  val code = translator.translate(f,"agent")
//  println(code)



  val system: ActorSystem[IMessage] = typed.ActorSystem(MAS(), "MAS")
  val resolver : ActorRefResolver = ActorRefResolver(system)

  def main(args: Array[String]): Unit = {

//    import org.apache.log4j.BasicConfigurator
//    BasicConfigurator.configure()




//    val system: ActorSystem[IMessage] =
//      typed.ActorSystem(MAS(), "MAS")


//    system ! AgentRequestMessage(
//      Seq(
//        AgentRequest(FactoryManager.create("/home/msotafa/IdeaProjects/actor-playgrounds/grounds/src/main/asl/kyc/client.asl","client"), "client", 4),
//        AgentRequest(FactoryManager.create("/home/msotafa/IdeaProjects/actor-playgrounds/grounds/src/main/asl/kyc/bank.asl","bank"), "ing", 1),
//        AgentRequest(FactoryManager.create("/home/msotafa/IdeaProjects/actor-playgrounds/grounds/src/main/asl/kyc/bank.asl","bank"), "abn", 1),
//        AgentRequest(FactoryManager.create("/home/msotafa/IdeaProjects/actor-playgrounds/grounds/src/main/asl/kyc/employee.asl","employee"), "employee", 2)
//      ))

//    system ! AgentRequestMessage(
//      Seq(
//        AgentRequest(FactoryManager.create("/home/msotafa/IdeaProjects/actor-playgrounds/grounds/src/main/asl/agere2020/benchmark1/listener.asl","listener"), "listener", 1000),
//        AgentRequest(FactoryManager.create("/home/msotafa/IdeaProjects/actor-playgrounds/grounds/src/main/asl/agere2020/benchmark1/talker.asl","talker"), "talker", 1),
//      ))
//
//    system ! AgentRequestMessage(
//      Seq(
////        AgentRequest(FactoryManager.create("/home/msotafa/IdeaProjects/actor-playgrounds/grounds/src/main/asl/agere2020/benchmark2/listener.asl","listener"), "listener", 1000),
//        AgentRequest(benchmark3.talker.Agent, "talker", 10),
//      ))


  }


}
