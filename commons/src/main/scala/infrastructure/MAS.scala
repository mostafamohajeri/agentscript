package infrastructure

import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, ActorRefResolver, Behavior}
import infrastructure.access.ASHttpServer
import scala.collection.parallel.CollectionConverters._

object MAS {

  def apply(): Behavior[IMessage] = {
    Behaviors.setup { context =>
      {
//      ASHttpServer.create(context)
        var agentsNotStarted: Seq[ActorRef[IMessage]] = Seq()
        var agentsNotInitialized: Int                 = 0
        val resolver                                  = ActorRefResolver(context.system)
        var yellowPages: ActorRef[IMessage]           = context.spawn(YellowPages.apply(), "yp");
        yellowPages ! ActorSubscribeMessage("__MAS", context.self)
        Behaviors.receive { (context, message) =>
          message match {
            case AgentRequestMessage(types, respondTo) =>
              types foreach {
                case AgentRequest(agentType, name_pattern, count) =>
                  agentsNotInitialized += count
                  for (a <- 1 to count) {
                    val name = if (count == 1) name_pattern else name_pattern + a
                    val ref  = context.spawn(agentType.apply(name, yellowPages, context.self), name)
                    agentsNotStarted = agentsNotStarted :+ ref
                    yellowPages ! ActorSubscribeMessage(name, ref)
                  }
                  if (respondTo != null)
                    respondTo ! AgentRequestRespondMessage(agentsNotStarted)
              }
              Behaviors.same
            case ReadyMessage(s) =>
              agentsNotInitialized -= 1
              if (agentsNotInitialized == 0) {
                agentsNotStarted.par.foreach(a => a ! StartMessage())
                agentsNotStarted = Seq[ActorRef[IMessage]]()
              }
              Behaviors.same
            case SystemExitMessage() =>
              Behaviors.stopped

            case _ => throw new RuntimeException(f"can no handle message of type $message")
          }

        }
      }
    }
  }
}
