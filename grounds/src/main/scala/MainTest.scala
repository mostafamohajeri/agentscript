

import agentfactory.{FactoryCompiler, FactoryManager}
import akka.actor.typed
import akka.actor.typed.ActorSystem
import infrastructure.{AgentRequest, AgentRequestMessage, IMessage, MAS}
import translation.Translator

import scala.reflect.runtime.universe



object MainTest {





  def getTypeTag[T: universe.TypeTag](obj: T) = universe.typeTag[T]

  def main(args: Array[String]): Unit = {

    import org.apache.log4j.BasicConfigurator
    BasicConfigurator.configure()

    val system: ActorSystem[IMessage] =
      typed.ActorSystem(MAS(), "MAS")


    system ! AgentRequestMessage(
      Seq(
        AgentRequest(FactoryManager.create("/home/msotafa/IdeaProjects/actor-playgrounds/grounds/src/main/asl/kyc/client.asl","client"), "client", 4),
        AgentRequest(FactoryManager.create("/home/msotafa/IdeaProjects/actor-playgrounds/grounds/src/main/asl/kyc/bank.asl","bank"), "ing", 1),
        AgentRequest(FactoryManager.create("/home/msotafa/IdeaProjects/actor-playgrounds/grounds/src/main/asl/kyc/bank.asl","bank"), "abn", 1),
        AgentRequest(FactoryManager.create("/home/msotafa/IdeaProjects/actor-playgrounds/grounds/src/main/asl/kyc/employee.asl","employee"), "employee", 2)
      ))


  }


}

