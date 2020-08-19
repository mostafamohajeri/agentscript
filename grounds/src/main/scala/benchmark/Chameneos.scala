package benchmark

import akka.actor.typed
import akka.actor.typed.ActorSystem
import infrastructure.{AgentRequest, AgentRequestMessage, IMessage, MAS}

import scala.language.implicitConversions

object Chameneos {


  def main(args: Array[String]): Unit = {
    import org.apache.log4j.BasicConfigurator
    BasicConfigurator.configure()

    if(args.length < 2)
      {
        println("please provide the needed args")
        return
      }

    cham_data.nb_chameneos = args(1).toInt
    cham_data.nb_meetings = args(0).toInt


    val system: ActorSystem[IMessage] = typed.ActorSystem(MAS(), "MAS")

    system ! AgentRequestMessage(
      Seq(
        AgentRequest(broker.Agent, "broker", 1),
        AgentRequest(cham.Agent, "cham", cham_data.nb_chameneos),
      )
    )

  }

}
