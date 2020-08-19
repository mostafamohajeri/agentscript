package benchmark

import akka.actor.typed
import akka.actor.typed.ActorSystem
import infrastructure.{AgentRequest, AgentRequestMessage, IMessage, MAS}

import scala.language.implicitConversions

object Token_ring {


  def main(args: Array[String]): Unit = {
    //import org.apache.log4j.BasicConfigurator
    //BasicConfigurator.configure()

    if(args.length < 2)
      {
        println("please provide the needed args")
        return
      }

    ring_data.nb_agents = args(1).toInt
    ring_data.nb_tokens = args(0).toInt

    if(args.length > 2)
      ring_data.nb_hops = args(2).toInt

    val system: ActorSystem[IMessage] = typed.ActorSystem(MAS(), "MAS")

    system ! AgentRequestMessage(
      Seq(
        AgentRequest(master.Agent, "master", 1),
        AgentRequest(thread.Agent, "thread", ring_data.nb_agents),
      )
    )

  }

}
