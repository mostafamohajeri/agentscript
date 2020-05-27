import akka.actor.typed
import akka.actor.typed.ActorSystem

object Main extends App {
  val system: ActorSystem[IMessage] =
    typed.ActorSystem(MAS(), "MAS")
  system ! AgentRequest(GeneratedAgent1.Agent,1)
  system ! StartMessage()
}
