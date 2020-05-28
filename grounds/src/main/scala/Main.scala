import akka.actor.typed
import akka.actor.typed.ActorSystem

object Main {

  def main(args: Array[String]): Unit = {
    val system: ActorSystem[IMessage] =
      typed.ActorSystem(MAS(), "MAS")
    system ! AgentRequest(Map(GeneratedAgent1.Agent -> 100))
//    system ! StartMessage()
  }

}