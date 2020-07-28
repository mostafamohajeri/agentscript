

import akka.actor.typed
import akka.actor.typed.{ActorRefResolver, ActorSystem}
import infrastructure.{AgentRequest, AgentRequestMessage, IMessage, MAS}

object Main {

  //  val gtSignature = new Signature("gt", 2,false)


  def main(args: Array[String]): Unit = {
    import org.apache.log4j.BasicConfigurator
    BasicConfigurator.configure()


    val system: ActorSystem[IMessage] = typed.ActorSystem(MAS(), "MAS")
    val resolver : ActorRefResolver = ActorRefResolver(system)

    system ! AgentRequestMessage(
      Seq(
        AgentRequest(client.Agent, "client", 4),
        AgentRequest(bank.Agent, "ing", 1),
        AgentRequest(bank.Agent, "abn", 1),
        AgentRequest(employee.Agent, "employee", 2)
      )
    )

  }

}
